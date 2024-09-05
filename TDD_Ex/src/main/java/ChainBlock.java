public interface ChainBlock extends Iterable<Transaction> {

    int getCount();

    void add(Transaction transaction);

    boolean contains(Transaction transaction);

    boolean contains(int id);

    void changeTransactionStatus(int id, Status newStatus);

    void removeTransactionById(int id);

    Transaction getById(int id);

    Iterable<Transaction> getByTransactionStatus(Status status);

    Iterable<String> getAllSendersWithTransactionStatus(Status status);

    Iterable<String> getAllReceiversWithTransactionStatus(Status status);

    Iterable<Transaction> getAllOrderedByAmountDescendingThenById();

    Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender);

    Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver);

    Iterable<Transaction> getByTransactionStatusAndMaximumAmount(Status status, double amount);

    Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount);

    Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi);

    Iterable<Transaction> getAllInAmountRange(double lo, double hi);

}
