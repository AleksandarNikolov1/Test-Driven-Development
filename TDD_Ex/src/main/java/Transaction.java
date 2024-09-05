public interface Transaction {
    int getId();
    void setTransactionStatus(Status transactionStatus);
    Status getStatus();
    double getAmount();
    String getSender();
    String getReceiver();
}



