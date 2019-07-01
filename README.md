# Analyze
1. [Сборка](#build)
2. [Запуск приложения](#run)
   * [Пример команды запуска](#command)
     * [Windows PowerShell](#windows)
     * [Linux](#linux)
### Сборка <a name="build"></a>
Сборка конечного приложения может быть выполнена командой: 
`mvn clean install`.
### Запуск приложения <a name="run"></a>
#### Пример команды запуска <a name="command"></a>:
##### Windows PowerShell <a name="windows"></a>
 `Get-Content access.log | java -jar analyze.jar -t 45 -u 99.9`
##### Linux <a name="linux"></a> 
`cat access.log | java -jar analyze.jar -t 45 -u 99.9`