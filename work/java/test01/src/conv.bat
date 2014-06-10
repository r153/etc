del mySemantics.bak
ren mySemantics.java mySemantics.bak

"C:\Program Files\Java\jdk1.6.0_26\bin\java" ^
 -cp %CLASSPATH%;..\lib\mousejdk1_6.jar ^
   mouse.Generate -G myGrammar.txt -P myParser -S mySemantics -s

