
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionPool {

    private HashMap<ByteArrayWrapper, Transaction> H;

    public TransactionPool() {
        H = new HashMap<ByteArrayWrapper, Transaction>();
    }

    public TransactionPool(TransactionPool txPool) {
        H = new HashMap<ByteArrayWrapper, Transaction>(txPool.H);
    }

    public void addTransaction(Transaction tx) {
        ByteArrayWrapper hash = new ByteArrayWrapper(tx.getHash());
        H.put(hash, tx);
    }

    public void removeTransaction(byte[] txHash) {
        ByteArrayWrapper hash = new ByteArrayWrapper(txHash);
        H.remove(hash);
    }

    public Transaction getTransaction(byte[] txHash) {
        ByteArrayWrapper hash = new ByteArrayWrapper(txHash);
        return H.get(hash);
    }

    public ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> T = new ArrayList<Transaction>();
        for (Transaction tx : H.values())
            T.add(tx);
        return T;
    }
}
