import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ChainBlockImpl implements ChainBlock {

    private List<Transaction> transactions;

    public ChainBlockImpl() {
        transactions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public boolean contains(Transaction transaction) {
        return transactions.contains(transaction);
    }

    @Override
    public boolean contains(int id) {
        Transaction transaction = transactions.stream().filter(t -> t.getId() == id)
                .findFirst().orElse(null);

        return transaction != null;
    }

    @Override
    public void changeTransactionStatus(int id, Status newStatus) {
        transactions.stream().filter(t -> t.getId() == id)
                .findFirst().orElseThrow(IllegalArgumentException::new).setTransactionStatus(newStatus);
    }

    @Override
    public void removeTransactionById(int id) {
        Transaction transaction = transactions.stream().filter(t -> t.getId() == id)
                .findFirst().orElseThrow(IllegalArgumentException::new);

        transactions.remove(transaction);
    }


    @Override
    public Transaction getById(int id) {
        return transactions.stream().filter(t -> t.getId() == id)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Iterable<Transaction> getByTransactionStatus(Status status) {
        List<Transaction> transactionsByStatus = new ArrayList<>();
        transactions.stream()
                .filter(t -> t.getStatus().equals(status))
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .forEach(transactionsByStatus::add);

        if (transactionsByStatus.size() == 0)
            throw new IllegalArgumentException();

        return transactionsByStatus;
    }

    @Override
    public Iterable<String> getAllSendersWithTransactionStatus(Status status) {
        List<String> senders = new ArrayList<>();

        transactions.stream().filter(t -> t.getStatus().equals(status))
                .forEach(t -> senders.add(t.getSender()));

        if (senders.size() == 0)
            throw new IllegalArgumentException();

        return senders;
    }

    @Override
    public Iterable<String> getAllReceiversWithTransactionStatus(Status status) {
        List<String> receivers = new ArrayList<>();

        transactions.stream().filter(t -> t.getStatus().equals(status))
                .forEach(t -> receivers.add(t.getReceiver()));

        if (receivers.size() == 0)
            throw new IllegalArgumentException();

        return receivers;
    }

    @Override
    public Iterable<Transaction> getAllOrderedByAmountDescendingThenById() {
        return transactions.stream()
                .sorted((t1, t2) -> {
                    int result = Double.compare(t2.getAmount(), t1.getAmount());
                    if (result == 0) {
                        result = Integer.compare(t2.getId(), t1.getId());
                    }

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender) {
        List<Transaction> transactionsBySender = transactions.stream()
                .filter(t -> t.getSender().equals(sender)).sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .collect(Collectors.toList());

        if (transactionsBySender.size() == 0)
            throw new IllegalArgumentException();

        return transactionsBySender;
    }

    @Override
    public Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver) {
        List<Transaction> transactionsByReceiver = transactions.stream()
                .filter(t -> t.getReceiver().equals(receiver))
                .sorted((t1, t2) -> {
                    int result = Double.compare(t1.getAmount(), t2.getAmount());

                    if (result == 0){
                        result = Integer.compare(t1.getId(), t2.getId());
                    }

                    return result;
                })
                .collect(Collectors.toList());

        if (transactionsByReceiver.size() == 0)
            throw new IllegalArgumentException();

        return transactionsByReceiver;
    }

    @Override
    public Iterable<Transaction> getByTransactionStatusAndMaximumAmount(Status status, double amount) {
        return transactions.stream()
                .filter(t -> t.getStatus().equals(status) && t.getAmount() <= amount)
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount) {
        List<Transaction> transactionsBySenderAndMinAmountDesc = transactions.stream()
                .filter(t -> t.getSender().equals(sender) && t.getAmount() > amount)
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .collect(Collectors.toList());

        if (transactionsBySenderAndMinAmountDesc.size() == 0)
            throw new IllegalArgumentException();

        return transactionsBySenderAndMinAmountDesc;
    }

    @Override
    public Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi) {
        List<Transaction> transactionsByReceiverAndAmountRange = transactions.stream()
                .filter(t -> t.getReceiver().equals(receiver) && (t.getAmount() >= lo && t.getAmount() < hi))
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .collect(Collectors.toList());

        if (transactionsByReceiverAndAmountRange.isEmpty())
            throw new IllegalArgumentException();

        return transactionsByReceiverAndAmountRange;
    }

    @Override
    public Iterable<Transaction> getAllInAmountRange(double lo, double hi) {
        return transactions.stream().filter(t -> t.getAmount() >= lo && t.getAmount() <= hi)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Transaction> iterator() {
        return new Iterator<Transaction>() {

            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < transactions.size();
            }

            @Override
            public Transaction next() {
                return transactions.get(index++);
            }
        };
    }
}
