# Compiladores - Linguagem Jvlal

Equipe: Lucas A. Lisboa e João Victor Ferro

## Passo a Passo para Executar o Analisador Léxico (Ubuntu)
1. Certifique-se que sua máquina possui os pacotes necessários do Java instalados. Caso não, use os seguintes comandos:
   
   ``` sudo apt install  openjdk-11-jre-headless ```
   
   ``` sudo apt install  openjdk-11-jdk-headless ```

2. Baixe este repositório;
3. Leve o arquivo .zip baixado para a raiz do seu sistema;
4. Descompacte o arquivo;
5. Abra o terminal;
6. Para ir à pasta que o programa deverá ser executado, utilize o comando:

   ``` cd Compiladores-main/Analisador_Lexico ```

7. Para compilar o programa, utilize o comando:

   ``` javac src.main.MainClass.java ```
   
8. Para executar o programa, utilize o comando: 

   ``` java src.main.MainClass arquivo_de_entrada```

   Sendo `arquivo_de_entrada` o nome do arquivo que será a entrada do programa (o arquivo deve estar dentro da pasta Analisador_Lexico). Exemplo: aloMundo, Fibonacci, shellSort.
