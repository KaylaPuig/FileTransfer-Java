# FileTransfer-Java

A barebones encrypted file sharing application written in Java for the terminal.

The program runs under the assumption that the sender and receiver both know the password to be used for this one-time transfer.

Do not assume this program is secure in any meaningful sense, instead use simpler tools such as SFTP or SCP.

This program was created for fun to demonstrate the usage of Java's standard socket and encryption implementations.

```
Usage: filetransfer [options] [arguments]
Options:
    -h, --help                          Display this help message
    -a, --address [address:port|port]   Specify address:port combination, or port only for sending
    -f, --filename                      Specify sent/received file name
    -p, --password                      Set the password for encryption/decryption
Arguments:
    [send|receive]                      Run the program in send or receive mode for the given options
```
