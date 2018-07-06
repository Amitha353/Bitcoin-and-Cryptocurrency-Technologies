# Bitcoin-and-Cryptocurrency-Technologies
* Crypto and Cryptocurrencies
* Bitcoin achieves Decentralization
* Bitcoin Mechanism
* Store and Use Bitcoin
* Bitcoin Mining
* Bitcoin Anonymity
* Community, Politics and Regulations
------------------------------------------------------
## 1. Crypto and Cryptocurrencies
* Cryptographic Hash function - mathematical function and takes three attributes: takes any string as input; fixed-size output(256 bits); efficiently computable;
#### Hash functions need to be cryptographically secure (Security properties):
 1. Collision-free - If x != y , then we can assume H(x) != H(Y);
 2. Hiding- Given H(x), infeasible to find x.For common value x, Take x and concatenate it with a value r.
     (com, key) := commit(msg)
     match := verify(com, key, msg)
 3. Puzzle-friendly - For every possible output value y, if k is chosen from a distribution with high min-entropy(widely spread out distribution), then it is infeasible to find x such that H(k | x) = y; 

#### SHA-256 hash function
* Takes the message (256 bits) being hashed and breaks it into blocks of 512 bits long;
* Since the message is not necessarily gonna be a multiple of the block-size some padding needs to be appended.
* The padding consist of - 64 bit field at end - before that 1 bit - followed by some 0 bits(until 512 msg block - rest chopped off;

#### Hash Pointer - Data structure - pointer to where information is stored and where a cryptographic hash of the information can be stored.
* Regular pointer gives a way to retrieve the information, while hash pointer gives a means to retrieve the information as well as verify if the information has changed.

#### Merkel tree: Binary tree with Hash Pointers - Ralph Merkel
* Each of these blocks are hashed using some hash function. Then each pair of nodes are recursively hashed until we reach the root node, which is a hash of all nodes below it.
* This takes (log n) items to be shown and therefore (log n) time to verify;

#### Digital Signatures : (i) Only you can sign, but anyone can verify; (ii) Signature is tied to a particular document;
##### 3 Operations performed - API digital signature
* Generate keys -> provide the input keysize and this generates two keys sk and pk. (sk, pk) := generateKeys(keysize); randomized Algo
* Sign Operation -> sig := sign(sk, message); (sk : secret signing key | pk: public verification key | sig (signature)); random Algo
* Verify Operation -> isValid := verify(pk, message, sig); deterministic Algo;

#### GoofyCoin -> simplest cryptocurrency engine;
* Goofy creates new coins - digital signature of Goofy and anybody can verify it;
* Coins owner can spend it, recepient can pass it to someone else, but double spend can occur;

#### ScroogeCoin 
* Publishes a history of all transaction - block-chain digitally signed by Scrooge;
* 2 kinds of transactions : Createcoin transaction and Paycoin transaction;
##### Rules PayCoins transactions:
1. consumed coins are valid - (the coins were created in previous transactions);
2. consumed coins were not already consumed in some previous transactions (double spend attack);
3. total values of the coins out of the transaction is same as the total coins that went into the transaction.
4. the transaction is signed by all the owners of the consmed coins.
------------------------------------------------------
------------------------------------------------------
## 2. Bitcoin achieves Decentralization

#### Distributed consensus :
* Achieve overall system reliability in the presence of a number of faulty processes which requires processes to agree on some data value that is needed during computation. Examples whether to commit a transaction to a database, etc;

#### "peer to peers" system : 
* They are computer systems/ hardwares which are connected to each other via the Internet.

#### How consensus works in Bitcoin : 
* All nodes have a sequences of blocks of transactions they've reached consensus on.
* Each node has a set of outstanding transactions it's heard about.
* Could have a sequence of blocks that everybody has agreed upon. (Block - A sequence of transactions);

###### Consensus could be hard : Nodes may crash, be malicious, network could be imperfect(crash, latency), etc;
#### Bitcoin nodes have no identities :
* Identity is hard in a  P2P (peer-to-peer) system (decentralized) (no central authority to assign and verify node creation)
* Pseudonymity is a goal of Bitcoin.

#### Consensus Algorithm:
1. New transactions are broadcast to all nodes.
2. Each node collects new transactions into a block.
3. In each round a random node gets to broadcast its block.
4. Other nodes accept the block only if all the transactions in it are valid (unspent, valid crypto signatures, double spend);
5. Nodes express their acceptance of the block by including the hash in the next block they create.

#### Incentives: 
* Incentive 1 : Block reward - Include a special coin-create transaction in the block; Choose a recipient address of this transaction;(12.5 BTC)
* Incentive 2 : Transaction fee - transaction creator can make output value more than the input value, where the remainder is payed as transaction fee.

#### Proof of Work:
* Instead of picking a node, in a decentralized network can allow nodes to compete using their computing power and make it hard to create new identities.

#### Hash puzzles:
* Each block has a "nonce", "previous hash" -> points to the previous block, list of transactions.
* For a node to successfully create a block it must create a hash output that matches that target size and this demands high computation power and is not very easy. 
To create a block , find nonce s.t.
###### H(nonce || prev_hash || tx || ... || tx) is very small

#### 3 properties of POW:
* PoW property 1 :  difficult to compute - about 10^20 hashes/block; (target space - 1 / 10^20 of the output space);
* PoW property 2 : parametrizable cost - Nodes on the peer-to-peer network will automatically re-calculate the target; Goal : average time between blocks = 10 minutes; Mean time to find a block = 10 minutes / fraction of hash power;
* PoW propert 3 : trivial to verify -> gets rid of the centalization property - Nonce must be published as part of block; Other miners simply verify that
    H(nonce || prev_hash || tx || ... || tx) < target
    
### Terminology
--------------
1. Identities - No identities needed, any user can create a pseudonymous key pair at any moment , and any number of them.
2. Transactions - Messages that are broadcast to the Bitcoin peer-to-peer network - instructions to transfer a coin rfom one address to another.
3. P2P network - propagates all new transactions/blocks to all the Bitcoin peer nodes.
4. Block-chain & Consensus -  security . transaction in the block-chain only after confirmations (>6) - part of the consensus chain;
 Orphan block - blocks not a part of the consensus chain - due to double spend, network latency, invalid bliock, etc;
5. Hash Puzzles and Mining - Miners are special type of nodes that compete for creating new blocks and are rewarded for their efforts in terms of Bitcoins.    

#### Bitcoin consensus gives us:
1. Append-only ledger - datastructure that we can only write to and once data is there , it's forever.
2. Decentralized consensus - decentralized protocol for establishing the value of that ledger.
3. Miners to validate transactions. (no double spends);
------------------------------------------------------
------------------------------------------------------
## 3. Bitcoin Mechanism

* Bitcoin Transactions -> Transaction-based ledger
#### A transaction-based ledger (Bitcoin) :
* Transactions explicitly specify the number of inputs and the number of outputs.
* Transactions have unique identifier - "hash pointer".
* Mandatory to consume the output of the previous transaction.
* Requires a finite scan to verify if the transaction is valid.

#### Join Payment :
* A transaction can be signed by two members and output to a particular address. It must be signed by both the inpute members. - Joint Payment (2 inputs, 1 outputs) - Joint Payment (2 inputs, 1 outputs)

#### Real Bitcoin Transaction: 3 parts
1. Metadata : transaction hash, housekeeping (version, locktime, size, etc);
2. Input : previous transactions, signatures;
3. Output : calue, script - public key, verify and checksig operations.

------------------------------------------------------
------------------------------------------------------
## 4. Store and Use Bitcoin
------------------------------------------------------
------------------------------------------------------
## 5. Bitcoin Mining
------------------------------------------------------
------------------------------------------------------
## 6. Bitcoin Anonymity
------------------------------------------------------
------------------------------------------------------
## 7. Community, Politics and Regulations
------------------------------------------------------
 
