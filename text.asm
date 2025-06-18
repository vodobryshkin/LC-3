ADD R1, R2, R3
AND R4, R4, #0
BRn LOOP
JMP R7
LD R0, VALUE
LDI R1, POINTER
LEA R2, ARRAY
NOT R3, R4
ST R5, RESULT
TRAP x25
.HALT
START: ADD R1, R2, #1
LOOP: BRn LOOP
END_PROG: .HALT
.ORIG x3000
.FILL xABCD
.BLKW 10
.STRINGZ "Hello, LC-3!"
.END
; Это полностю комментарий
ADD R1, R2 ; Частичный комментарий
LOOP: BRzp LOOP ; Бесконечный цикл
.ORIG x3000 ; Начало программы


LABEL: .FILL #42 ; Инициализация значения
    LEA R0, MSG ; Загрузка адреса
    TRAP x22     ; Вывод строки
R1, R2, R3
ADD R1, R8
.UNKNOWN x123
LABEL