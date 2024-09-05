public class TransactionImpl implements Comparable<TransactionImpl>, Transaction{

    private int id;
    private Status status;
    private String from;
    private String to;
    private double amount;

    public TransactionImpl(int id, Status status, String from, String to, double amount) {
        this.id = id;
        this.status = status;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int compareTo(TransactionImpl o) {
        return 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setTransactionStatus(Status transactionStatus) {
        this.status = transactionStatus;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getSender() {
        return from;
    }

    @Override
    public String getReceiver() {
        return to;
    }
}


