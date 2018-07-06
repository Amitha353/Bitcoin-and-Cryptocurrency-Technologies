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

##### Hash Pointer - Data structure - pointer to where information is stored and where a cryptographic hash of the information can be stored.
* Regular pointer gives a way to retrieve the information, while hash pointer gives a means to retrieve the information as well as verify if the information has changed.
------------------------------------------------------
------------------------------------------------------
## 2. Bitcoin achieves Decentralization
------------------------------------------------------
------------------------------------------------------
## 3. Bitcoin Mechanism
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
 
