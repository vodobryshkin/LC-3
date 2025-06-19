; Тест сложения 2 + 3
        .ORIG x3000
        AND R0, R0, #0    ; Очищаем R0
        ADD R0, R0, #2    ; R0 = 2
        ADD R1, R0, #3    ; R1 = R0 + 3 = 5
        .HALT
        .END