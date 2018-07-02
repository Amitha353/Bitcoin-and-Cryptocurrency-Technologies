
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class Transaction {

    public class Input {
        /** hash of the Transaction whose output is being used */
        public byte[] prevTxHash;
        /** used output's index in the previous transaction */
        public int outputIndex;
        /** the signature produced to check validity */
        public byte[] signature;

        public Input(byte[] prevHash, int index) {
            if (prevHash == null)
                prevTxHash = null;
            else
                prevTxHash = Arrays.copyOf(prevHash, prevHash.length);
            outputIndex = index;
        }

        public void addSignature(byte[] sig) {
            if (sig == null)
                signature = null;
            else
                signature = Arrays.copyOf(sig, sig.length);
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }

            Input in = (Input) other;

            if (prevTxHash.length != in.prevTxHash.length)
                return false;
            for (int i = 0; i < prevTxHash.length; i++) {
                if (prevTxHash[i] != in.prevTxHash[i])
                    return false;
            }
            if (outputIndex != in.outputIndex)
                return false;
            if (signature.length != in.signature.length)
                return false;
            for (int i = 0; i < signature.length; i++) {
                if (signature[i] != in.signature[i])
                    return false;
            }
            return true;
        }

        public int hashCode() {
            int hash = 1;
            hash = hash * 17 + Arrays.hashCode(prevTxHash);
            hash = hash * 31 + outputIndex;
            hash = hash * 31 + Arrays.hashCode(signature);
            return hash;
        }
    }

    public class Output {
        /** value in bitcoins of the output */
        public double value;
        /** the address or public key of the recipient */
        public PublicKey address;

        public Output(double v, PublicKey addr) {
            value = v;
            address = addr;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }

            Output op = (Output) other;

            if (value != op.value)
                return false;
            if (!((RSAPublicKey) address).getPublicExponent().equals(
                    ((RSAPublicKey) op.address).getPublicExponent()))
                return false;
            if (!((RSAPublicKey) address).getModulus().equals(
                    ((RSAPublicKey) op.address).getModulus()))
                return false;
            return true;
        }

        public int hashCode() {
            int hash = 1;
            hash = hash * 17 + (int) value * 10000;
            hash = hash * 31 + ((RSAPublicKey) address).getPublicExponent().hashCode();
            hash = hash * 31 + ((RSAPublicKey) address).getModulus().hashCode();
            return hash;
        }
    }

    /** hash of the transaction, its unique id */
    private byte[] hash;
    private ArrayList<Input> inputs;
    private ArrayList<Output> outputs;
    private boolean coinbase;

    public Transaction() {
        inputs = new ArrayList<Input>();
        outputs = new ArrayList<Output>();
        coinbase = false;
    }

    public Transaction(Transaction tx) {
        hash = tx.hash.clone();
        inputs = new ArrayList<Input>(tx.inputs);
        outputs = new ArrayList<Output>(tx.outputs);
        coinbase = false;
    }

    /** create a coinbase transaction of value {@code coin} and calls finalize on it */
    public Transaction(double coin, PublicKey address) {
        coinbase = true;
        inputs = new ArrayList<Input>();
        outputs = new ArrayList<Output>();
        addOutput(coin, address);
        finalize();
    }

    public boolean isCoinbase() {
        return coinbase;
    }

    public void addInput(byte[] prevTxHash, int outputIndex) {
        Input in = new Input(prevTxHash, outputIndex);
        inputs.add(in);
    }

    public void addOutput(double value, PublicKey address) {
        Output op = new Output(value, address);
        outputs.add(op);
    }

    public void removeInput(int index) {
        inputs.remove(index);
    }

    public void removeInput(UTXO ut) {
        for (int i = 0; i < inputs.size(); i++) {
            Input in = inputs.get(i);
            UTXO u = new UTXO(in.prevTxHash, in.outputIndex);
            if (u.equals(ut)) {
                inputs.remove(i);
                return;
            }
        }
    }

    public byte[] getRawDataToSign(int index) {
        // ith input and all outputs
        ArrayList<Byte> sigData = new ArrayList<Byte>();
        if (index > inputs.size())
            return null;
        Input in = inputs.get(index);
        byte[] prevTxHash = in.prevTxHash;
        ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
        b.putInt(in.outputIndex);
        byte[] outputIndex = b.array();
        if (prevTxHash != null)
            for (int i = 0; i < prevTxHash.length; i++)
                sigData.add(prevTxHash[i]);
        for (int i = 0; i < outputIndex.length; i++)
            sigData.add(outputIndex[i]);
        for (Output op : outputs) {
            ByteBuffer bo = ByteBuffer.allocate(Double.SIZE / 8);
            bo.putDouble(op.value);
            byte[] value = bo.array();
            byte[] addressExponent = ((RSAPublicKey) op.address).getPublicExponent().toByteArray();
            byte[] addressModulus = ((RSAPublicKey) op.address).getModulus().toByteArray();
            for (int i = 0; i < value.length; i++)
                sigData.add(value[i]);
            for (int i = 0; i < addressExponent.length; i++)
                sigData.add(addressExponent[i]);
            for (int i = 0; i < addressModulus.length; i++)
                sigData.add(addressModulus[i]);
        }
        byte[] sigD = new byte[sigData.size()];
        int i = 0;
        for (Byte sb : sigData)
            sigD[i++] = sb;
        return sigD;
    }

    public void addSignature(byte[] signature, int index) {
        inputs.get(index).addSignature(signature);
    }

    public byte[] getRawTx() {
        ArrayList<Byte> rawTx = new ArrayList<Byte>();
        for (Input in : inputs) {
            byte[] prevTxHash = in.prevTxHash;
            ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
            b.putInt(in.outputIndex);
            byte[] outputIndex = b.array();
            byte[] signature = in.signature;
            if (prevTxHash != null)
                for (int i = 0; i < prevTxHash.length; i++)
                    rawTx.add(prevTxHash[i]);
            for (int i = 0; i < outputIndex.length; i++)
                rawTx.add(outputIndex[i]);
            if (signature != null)
                for (int i = 0; i < signature.length; i++)
                    rawTx.add(signature[i]);
        }
        for (Output op : outputs) {
            ByteBuffer b = ByteBuffer.allocate(Double.SIZE / 8);
            b.putDouble(op.value);
            byte[] value = b.array();
            byte[] addressExponent = ((RSAPublicKey) op.address).getPublicExponent().toByteArray();
            byte[] addressModulus = ((RSAPublicKey) op.address).getModulus().toByteArray();
            for (int i = 0; i < value.length; i++)
                rawTx.add(value[i]);
            for (int i = 0; i < addressExponent.length; i++)
                rawTx.add(addressExponent[i]);
            for (int i = 0; i < addressModulus.length; i++)
                rawTx.add(addressModulus[i]);
        }
        byte[] tx = new byte[rawTx.size()];
        int i = 0;
        for (Byte b : rawTx)
            tx[i++] = b;
        return tx;
    }

    public void finalize() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getRawTx());
            hash = md.digest();
        } catch (NoSuchAlgorithmException x) {
            x.printStackTrace(System.err);
        }
    }

    public void setHash(byte[] h) {
        hash = h;
    }

    public byte[] getHash() {
        return hash;
    }

    public ArrayList<Input> getInputs() {
        return inputs;
    }

    public ArrayList<Output> getOutputs() {
        return outputs;
    }

    public Input getInput(int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return null;
    }

    public Output getOutput(int index) {
        if (index < outputs.size()) {
            return outputs.get(index);
        }
        return null;
    }

    public int numInputs() {
        return inputs.size();
    }

    public int numOutputs() {
        return outputs.size();
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        Transaction tx = (Transaction) other;
        // inputs and outputs should be same
        if (tx.numInputs() != numInputs())
            return false;

        for (int i = 0; i < numInputs(); i++) {
            if (!getInput(i).equals(tx.getInput(i)))
                return false;
        }

        if (tx.numOutputs() != numOutputs())
            return false;

        for (int i = 0; i < numOutputs(); i++) {
            if (!getOutput(i).equals(tx.getOutput(i)))
                return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < numInputs(); i++) {
            hash = hash * 31 + getInput(i).hashCode();
        }
        for (int i = 0; i < numOutputs(); i++) {
            hash = hash * 31 + getOutput(i).hashCode();
        }
        return hash;
    }
}
