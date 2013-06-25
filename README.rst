==================
Bitcoin Rock
==================

A 'Rock-Paper-Scissors-Lizard-Spock' game implementation for demonstrate 
Bitcoin capability for implement embedded payment systems and pay triggers
on software.

Properties file::

    account=account name on miner for save and receive money
    fee=fee for each round in float format with 8 precision positions
    endpoint=rpc miner endpoint
    port=rpc miner port
    user=rpc miner user
    password=rpc miner password

-----
Usage
-----

Once configured properties file, execute::

   $ java -jar bitcoin-rock-XXX.jar
   
-----
Rules
-----

Implemented features:

* User receives payments from system when machine loses.
* The program waits for payment when user loses.
* Configurable fee.