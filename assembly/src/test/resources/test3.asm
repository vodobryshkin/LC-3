        .ORIG x3000
        AND R1, R1, #0
        BRz GG
        ADD R1, R3, #7

GG:     ADD R3, R3, #6
E:      .HALT
        .END