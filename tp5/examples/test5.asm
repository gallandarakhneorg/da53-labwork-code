               #-- INPUT N
START:         PRINT @S0[PROGRAM_END+0]
               N[MEMORY_CONTEXT+0] = READ
               #-- LET F = 1
               F[MEMORY_CONTEXT+8] = 1
               #-- FOR I=2 TO N LET F = (F*I) NEXT I
               I[MEMORY_CONTEXT+16] = 2
L1:            @T1[MEMORY_CONTEXT+24] = (I[MEMORY_CONTEXT+16] <= N[MEMORY_CONTEXT+0])
               JMP L2[PROGRAM_START+320] IF NOT @T1[MEMORY_CONTEXT+24]
               #-- LET F = (F*I)
               @T2[MEMORY_CONTEXT+32] = (F[MEMORY_CONTEXT+8] * I[MEMORY_CONTEXT+16])
               F[MEMORY_CONTEXT+8] = @T2[MEMORY_CONTEXT+32]
               I[MEMORY_CONTEXT+16] = (I[MEMORY_CONTEXT+16] + 1)
               JMP L1[PROGRAM_START+128]
               #-- PRINT ((N+"! = ")+F)
L2:            @T3[MEMORY_CONTEXT+40] = (N[MEMORY_CONTEXT+0] + @S2[PROGRAM_END+5])
               @T4[MEMORY_CONTEXT+48] = (@T3[MEMORY_CONTEXT+40] + F[MEMORY_CONTEXT+8])
               PRINT @T4[MEMORY_CONTEXT+48]
               PRINT @S1[PROGRAM_END+3]
               #-- END
               EXIT
