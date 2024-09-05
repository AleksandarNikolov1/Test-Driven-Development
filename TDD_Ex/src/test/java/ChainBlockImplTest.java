import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ChainBlockImplTest {

    private static final int TRANSACTIONS_COUNT = 8;

    private ChainBlockImpl chainBlock;
    private static final Transaction transaction1 = new TransactionImpl(1, Status.FAILED, "Stan", "Aleks", 100.00);;
    private static final Transaction transaction2 = new TransactionImpl(2, Status.ABORTED, "Stan", "Aleks", 125.50);;
    private static final Transaction transaction3 = new TransactionImpl(3, Status.SUCCESSFUL, "Stan", "Aleks", 125.50);;
    private static final Transaction transaction4 = new TransactionImpl(4, Status.ABORTED, "Stan", "Aleks", 115.25);
    private static final Transaction transaction5 = new TransactionImpl(5, Status.FAILED, "Aleks", "Stan", 100.00);
    private static final Transaction transaction6 = new TransactionImpl(6, Status.ABORTED, "Aleks", "Stan", 125.50);
    private static final Transaction transaction7 = new TransactionImpl(7, Status.SUCCESSFUL, "Aleks", "Stan", 200.00);
    private static final Transaction transaction8 = new TransactionImpl(8, Status.SUCCESSFUL, "Aleks", "Stan", 115.25);

    @Before
    public void setUp(){
        chainBlock = new ChainBlockImpl();
    }

    private void addTransactionsToChainBlock() {
        chainBlock.add(transaction1);
        chainBlock.add(transaction2);
        chainBlock.add(transaction3);
        chainBlock.add(transaction4);
        chainBlock.add(transaction5);
        chainBlock.add(transaction6);
        chainBlock.add(transaction7);
        chainBlock.add(transaction8);
    }

    @Test
    public void testGetCountReturnsCorrectValue(){
        addTransactionsToChainBlock();
        assertEquals(TRANSACTIONS_COUNT, chainBlock.getCount());
    }

    @Test
    public void testAddShouldAddTransactionToChainBlock(){
        addTransactionsToChainBlock();
        Transaction transactionForValidation = chainBlock.getTransactions().get(0);
        assertEquals(1, transactionForValidation.getId());
        assertEquals(Status.FAILED, transactionForValidation.getStatus());
        assertEquals("Stan", transactionForValidation.getSender());
        assertEquals("Aleks", transactionForValidation.getReceiver());
        assertEquals(100.00, transactionForValidation.getAmount(), 0.01);
        assertEquals(8, chainBlock.getTransactions().size());
    }

    @Test
    public void testContainsReturnsCorrectValueByTransaction(){
        addTransactionsToChainBlock();
        assertTrue(chainBlock.contains(transaction4));

        Transaction newTransaction = new TransactionImpl(9, Status.FAILED,
                "Miro", "Tom", 15.50);
        assertFalse(chainBlock.contains(newTransaction));
    }

    @Test
    public void testContainsReturnsRightValueById(){
        addTransactionsToChainBlock();

        assertTrue(chainBlock.contains(1));
        assertFalse(chainBlock.contains(22));
    }

    @Test
    public void testChangeTransactionStatusById(){
        Transaction transaction = new TransactionImpl(777, Status.FAILED, "Aleks", "Stan", 115.25);

        chainBlock.add(transaction);
        chainBlock.changeTransactionStatus(transaction.getId(), Status.SUCCESSFUL);
        assertEquals(Status.SUCCESSFUL, transaction.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeTransactionStatusByIdFailsForInvalidId(){
        addTransactionsToChainBlock();

        chainBlock.changeTransactionStatus(-1, Status.SUCCESSFUL);
    }

    @Test
    public void testRemoveTransactionById(){
        addTransactionsToChainBlock();

        chainBlock.removeTransactionById(2);

        assertFalse(chainBlock.contains(transaction2));
        assertEquals(TRANSACTIONS_COUNT - 1, chainBlock.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveTransactionByIdFailsForInvalidId(){
        addTransactionsToChainBlock();

        chainBlock.removeTransactionById(-1);
    }

    @Test
    public void testGetByIdReturnsRightTransaction(){
        addTransactionsToChainBlock();

        assertEquals(transaction4, chainBlock.getById(4));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetByIdFailsForInvalidId(){
        addTransactionsToChainBlock();
        Transaction tr = chainBlock.getById(-1);
    }

    @Test
    public void testGetByTransactionStatusReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactionsByStatus
                = chainBlock.getByTransactionStatus(Status.SUCCESSFUL);

        for (Transaction transaction : transactionsByStatus) {
            assertEquals(Status.SUCCESSFUL, transaction.getStatus());
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetByTransactionStatusFailsForNoExistingStatus(){
        addTransactionsToChainBlock();
        Iterable<Transaction> transactionsByStatus
                = chainBlock.getByTransactionStatus(Status.UNAUTHORIZED);
    }

    @Test
    public void testGetAllSendersWithTransactionStatusReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<String> senders
                = chainBlock.getAllSendersWithTransactionStatus(Status.SUCCESSFUL);

        List<String> sendersList = new ArrayList<>();

        for (String sender : senders) {
            sendersList.add(sender);
        }

        assertEquals(3, sendersList.size());
        assertEquals("Stan", sendersList.get(0));
        assertEquals("Aleks", sendersList.get(1));
        assertEquals("Aleks", sendersList.get(2));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetAllSendersWithTransactionStatusFailsForNoExistingStatus(){
        addTransactionsToChainBlock();

        Iterable<String> senders
                = chainBlock.getAllSendersWithTransactionStatus(Status.UNAUTHORIZED);
    }

    @Test
    public void testGetAllReceiversWithTransactionStatusReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<String> receivers
                = chainBlock.getAllReceiversWithTransactionStatus(Status.SUCCESSFUL);

        List<String> receiversList = new ArrayList<>();

        for (String receiver : receivers) {
            receiversList.add(receiver);
        }

        assertEquals(3, receiversList.size());
        assertEquals("Aleks", receiversList.get(0));
        assertEquals("Stan", receiversList.get(1));
        assertEquals("Stan", receiversList.get(2));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetAllReceiversWithTransactionStatusFailsForNoExistingStatus(){
        addTransactionsToChainBlock();

        Iterable<String> receivers
                = chainBlock.getAllSendersWithTransactionStatus(Status.UNAUTHORIZED);
    }

    @Test
    public void testGetAllOrderedByAmountDescendingThenByIdReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> orderedTransactions =
                chainBlock.getAllOrderedByAmountDescendingThenById();

        double previousAmount = Double.MAX_VALUE;
        double previousId = Double.MAX_VALUE;

        for (Transaction transaction : orderedTransactions) {
            if (previousAmount != transaction.getAmount()) {
                assertTrue(transaction.getAmount() < previousAmount);
                previousAmount = transaction.getAmount();
            }

            else {
                assertTrue(transaction.getId() < previousId);
            }

            previousId = transaction.getId();
        }
    }

    @Test
    public void testGetBySenderOrderedByAmountDescendingReturnsRightCollection(){
        addTransactionsToChainBlock();

        double previousAmount = Double.MAX_VALUE;

        Iterable<Transaction> transactions = chainBlock.getBySenderOrderedByAmountDescending("Aleks");

        for (Transaction transaction : transactions) {
            assertTrue(previousAmount > transaction.getAmount());
            previousAmount = transaction.getAmount();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetBySenderOrderedByAmountDescendingFailsForNoExistingSender(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactions = chainBlock.getBySenderOrderedByAmountDescending("I don't exist");
    }

    @Test
    public void testGetByReceiverOrderedByAmountThenByIdReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactions = chainBlock.getByReceiverOrderedByAmountThenById("Stan");

        double previousAmount = Double.MIN_VALUE;
        double previousId = Double.MIN_VALUE;

        for (Transaction transaction : transactions) {
            if (previousAmount != transaction.getAmount()) {
                assertTrue(transaction.getAmount() > previousAmount);
                previousAmount = transaction.getAmount();
            }

            else {
                assertTrue(transaction.getId() > previousId);
            }

            previousId = transaction.getId();
        }
    }


    @Test (expected = IllegalArgumentException.class)
    public void testGetByReceiverOrderedByAmountThenByIdFailsForNoExistingSender(){
        addTransactionsToChainBlock();

        Iterable <Transaction> transactions = chainBlock.getByReceiverOrderedByAmountThenById("I don't exist");
    }

    @Test
    public void testGetByTransactionStatusAndMaximumAmountReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactions = chainBlock.getByTransactionStatusAndMaximumAmount(Status.SUCCESSFUL, 125.50);

        for (Transaction transaction : transactions) {
            assertEquals(Status.SUCCESSFUL, transaction.getStatus());
            assertTrue(125.50 >= transaction.getAmount());
        }
    }

    @Test
    public void testGetBySenderAndMinimumAmountDescendingReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactions = chainBlock.getBySenderAndMinimumAmountDescending("Stan", 110.00);

        double previousAmount = Double.MAX_VALUE;

        for (Transaction transaction : transactions) {
            assertEquals("Stan", transaction.getSender());
            assertTrue(110.00 < transaction.getAmount());
            assertTrue(previousAmount >= transaction.getAmount());
            previousAmount = transaction.getAmount();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBySenderAndMinimumAmountDescendingFailsForNoExistingSender(){
        addTransactionsToChainBlock();
        Iterable<Transaction> transactions = chainBlock.getBySenderAndMinimumAmountDescending("Someone", 110.00);
    }

    @Test
    public void testGetByReceiverAndAmountRangeReturnsRightCollection(){
        addTransactionsToChainBlock();

        Iterable<Transaction> transactions = chainBlock.getByReceiverAndAmountRange("Stan", 115.25, 200.00);


        for (Transaction transaction : transactions) {
            assertEquals("Stan", transaction.getReceiver());
            assertTrue(transaction.getAmount() >= 115.25 && transaction.getAmount() < 200);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByReceiverAndAmountRangeFailsForNoExistingSender(){
        addTransactionsToChainBlock();
        Iterable<Transaction> transactions = chainBlock.getByReceiverAndAmountRange("Someone", 115.25, 200.00);
    }

    @Test
    public void testGetAllInAmountRangeReturnsRightCollection(){
        addTransactionsToChainBlock();
        Iterable<Transaction> transactions = chainBlock.getAllInAmountRange(115.25, 200.00);

        for (Transaction transaction : transactions) {
            assertTrue(transaction.getAmount() >= 115.25 && transaction.getAmount() <= 200.00);
        }
    }
    
    @Test
    public void testIteratorIterateTransactions(){
        addTransactionsToChainBlock();

        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : chainBlock) {
            transactions.add(transaction);
        }

        assertEquals(chainBlock.getTransactions(), transactions);
        assertEquals(8, transactions.size());
    }
}
