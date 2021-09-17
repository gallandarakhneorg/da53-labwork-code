               #-- INPUT N
START:         PRINT @S0[PROGRAM_END+0]
               N[MEMORY_CONTEXT+0] = READ
               #-- LET F = 1
L3:            F[MEMORY_CONTEXT+8] = 1
               #-- LET I = 2
L4:            I[MEMORY_CONTEXT+16] = 2
               #-- WHILE (I<=N) DO GOSUB 1000 WEND
L1:            @T1[MEMORY_CONTEXT+24] = (I[MEMORY_CONTEXT+16] <= N[MEMORY_CONTEXT+0])
               JMP L2[PROGRAM_START+320] IF NOT @T1[MEMORY_CONTEXT+24]
               #-- GOSUB 1000
               PARAM 1000
               @T2[MEMORY_CONTEXT+32] = CALL BASICLINES[PROGRAM_START+672], 1
               CALL @T2[MEMORY_CONTEXT+32], 0
               JMP L1[PROGRAM_START+128]
               #-- PRINT ((N+"! = ")+F)
L2:            @T3[MEMORY_CONTEXT+40] = (N[MEMORY_CONTEXT+0] + @S2[PROGRAM_END+5])
               @T4[MEMORY_CONTEXT+48] = (@T3[MEMORY_CONTEXT+40] + F[MEMORY_CONTEXT+8])
               PRINT @T4[MEMORY_CONTEXT+48]
               PRINT @S1[PROGRAM_END+3]
               #-- END
L5:            EXIT
               #-- LET F = (F*I)
L6:            @T5[MEMORY_CONTEXT+56] = (F[MEMORY_CONTEXT+8] * I[MEMORY_CONTEXT+16])
               F[MEMORY_CONTEXT+8] = @T5[MEMORY_CONTEXT+56]
               #-- LET I = (I+1)
L7:            @T6[MEMORY_CONTEXT+64] = (I[MEMORY_CONTEXT+16] + 1)
               I[MEMORY_CONTEXT+16] = @T6[MEMORY_CONTEXT+64]
               #-- RETURN
L8:            RETURN
               EXIT
BASICLINES:    @T7[MEMORY_CONTEXT+72] = PARAM 0
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 10)
               JMP 832 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 0[PROGRAM_START+0]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 11)
               JMP 960 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 64[PROGRAM_START+64]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 12)
               JMP 1088 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 96[PROGRAM_START+96]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 13)
               JMP 1216 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 128[PROGRAM_START+128]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 14)
               JMP 1344 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 320[PROGRAM_START+320]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 15)
               JMP 1472 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 448[PROGRAM_START+448]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 1000)
               JMP 1600 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 480[PROGRAM_START+480]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 1001)
               JMP 1728 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 544[PROGRAM_START+544]
               RETURN @T8[MEMORY_CONTEXT+88]
               @T9[MEMORY_CONTEXT+80] = (@T7[MEMORY_CONTEXT+72] == 1002)
               JMP 1856 IF NOT @T9[MEMORY_CONTEXT+80]
               @T8[MEMORY_CONTEXT+88] = 608[PROGRAM_START+608]
               RETURN @T8[MEMORY_CONTEXT+88]
               PRINT @S3[PROGRAM_END+10]
               PRINT @T9[MEMORY_CONTEXT+80]
               PRINT @S1[PROGRAM_END+3]
               EXIT 1234
               RETURN @T8[MEMORY_CONTEXT+88]
