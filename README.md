# Java-embedded-TCL

The small demo has three apps(spring-boot):

Sender         Poroxy            Receiver
   |              |                 |
1. | --HTTP-->  TCL(ok)  --HTTP-->  |
   |              |                 |
2. | --HTTP-->  TCL(fail)           |
   |              |                 |
   |              |                 |

1. Sender includes HTTP header "Authorized: Ok" and Proxy TCL rules permit message to Receiver.

2. Sender omits valid authorization header and Proxy TCL rules reject message. 


