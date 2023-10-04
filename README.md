# Java-embedded-TCL

The small demo has tree apps(spring-boot):

Sender         Poroxy            Receiver
   |              |                 |
1. | --http-->  TCL(ok)  --http-->  |
   |              |                 |
2. | --http-->  TCL(fail)           |
   |              |                 |
   |              |                 |

1. Sender includes HTTP header "Authorized: Ok" and Proxy TCL rules permit message to Receiver.

2. Sender ommmit valid authorization header and Proxy TCL rules reject message. 


