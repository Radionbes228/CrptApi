package org.radion;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class CrptApi {
    private final HttpClient httpClient;
    private final Gson gson;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.semaphore = new Semaphore(requestLimit);
        this.scheduler = Executors.newScheduledThreadPool(1);

        long delay = timeUnit.toMillis(1);
        scheduler.scheduleAtFixedRate(() -> semaphore.release(requestLimit - semaphore.availablePermits()), delay, delay, TimeUnit.MILLISECONDS);
    }

    public void createDocument(Document document, String signature) throws Exception {
        semaphore.acquire();

        String json = gson.toJson(document);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .header("Content-Type", "application/json")
                .header("Signature", signature)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Не удалось создать документ: " + response.body());
        }
    }

    public static class Document {
        @SerializedName("description")
        private Description description;
        @SerializedName("doc_id")
        private String docId;
        @SerializedName("doc_status")
        private String docStatus;
        @SerializedName("doc_type")
        private String docType;
        @SerializedName("importRequest")
        private boolean importRequest;
        @SerializedName("owner_inn")
        private String ownerInn;
        @SerializedName("participant_inn")
        private String participantInn;
        @SerializedName("producer_inn")
        private String producerInn;
        @SerializedName("production_date")
        private String productionDate;
        @SerializedName("production_type")
        private String productionType;
        @SerializedName("products")
        private Product[] products;
        @SerializedName("reg_date")
        private String regDate;
        @SerializedName("reg_number")
        private String regNumber;

        public Description getDescription() {
            return description;
        }

        public String getDocId() {
            return docId;
        }

        public String getDocStatus() {
            return docStatus;
        }

        public String getDocType() {
            return docType;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public String getProductionType() {
            return productionType;
        }

        public Product[] getProducts() {
            return products;
        }

        public String getRegDate() {
            return regDate;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        public void setProducts(Product[] products) {
            this.products = products;
        }

        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }

        public static class Description {
            @SerializedName("participantInn")
            private String participantInn;

            public String getParticipantInn() {
                return participantInn;
            }

            public void setParticipantInn(String participantInn) {
                this.participantInn = participantInn;
            }
        }

        public static class Product {
            @SerializedName("certificate_document")
            private String certificateDocument;
            @SerializedName("certificate_document_date")
            private String certificateDocumentDate;
            @SerializedName("certificate_document_number")
            private String certificateDocumentNumber;
            @SerializedName("owner_inn")
            private String ownerInn;
            @SerializedName("producer_inn")
            private String producerInn;
            @SerializedName("production_date")
            private String productionDate;
            @SerializedName("tnved_code")
            private String tnvedCode;
            @SerializedName("uit_code")
            private String uitCode;
            @SerializedName("uitu_code")
            private String uituCode;

            public String getCertificateDocument() {
                return certificateDocument;
            }

            public void setCertificateDocument(String certificateDocument) {
                this.certificateDocument = certificateDocument;
            }

            public String getCertificateDocumentDate() {
                return certificateDocumentDate;
            }

            public void setCertificateDocumentDate(String certificateDocumentDate) {
                this.certificateDocumentDate = certificateDocumentDate;
            }

            public String getCertificateDocumentNumber() {
                return certificateDocumentNumber;
            }

            public void setCertificateDocumentNumber(String certificateDocumentNumber) {
                this.certificateDocumentNumber = certificateDocumentNumber;
            }

            public String getOwnerInn() {
                return ownerInn;
            }

            public void setOwnerInn(String ownerInn) {
                this.ownerInn = ownerInn;
            }

            public String getProducerInn() {
                return producerInn;
            }

            public void setProducerInn(String producerInn) {
                this.producerInn = producerInn;
            }

            public String getProductionDate() {
                return productionDate;
            }

            public void setProductionDate(String productionDate) {
                this.productionDate = productionDate;
            }

            public String getTnvedCode() {
                return tnvedCode;
            }

            public void setTnvedCode(String tnvedCode) {
                this.tnvedCode = tnvedCode;
            }

            public String getUitCode() {
                return uitCode;
            }

            public void setUitCode(String uitCode) {
                this.uitCode = uitCode;
            }

            public String getUituCode() {
                return uituCode;
            }

            public void setUituCode(String uituCode) {
                this.uituCode = uituCode;
            }
        }
    }
}
