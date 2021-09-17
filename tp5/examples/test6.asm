               #-- INPUT N
START:         PRINT @S0[PROGRAM_END+0]
               N[MEMORY_CONTEXT+0] = READ
               #-- LET F(1) = 1
               @T1[MEMORY_CONTEXT+24] = (1 - 1)
               @T1[MEMORY_CONTEXT+24] = (@T1[MEMORY_CONTEXT+24] * 8)
               F[MEMORY_CONTEXT+8](@T1[MEMORY_CONTEXT+24]) = 1
               #-- LET F(2) = 1
               @T2[MEMORY_CONTEXT+32] = (2 - 1)
               @T2[MEMORY_CONTEXT+32] = (@T2[MEMORY_CONTEXT+32] * 8)
               F[MEMORY_CONTEXT+8](@T2[MEMORY_CONTEXT+32]) = 1
               #-- FOR I=3 TO (N+1) LET F(I) = (F((I-1))+F((I-2))) NEXT I
               I[MEMORY_CONTEXT+16] = 3
L1:            @T3[MEMORY_CONTEXT+40] = (N[MEMORY_CONTEXT+0] + 1)
               @T4[MEMORY_CONTEXT+48] = (I[MEMORY_CONTEXT+16] <= @T3[MEMORY_CONTEXT+40])
               JMP L2[PROGRAM_START+832] IF NOT @T4[MEMORY_CONTEXT+48]
               #-- LET F(I) = (F((I-1))+F((I-2)))
               @T5[MEMORY_CONTEXT+56] = (I[MEMORY_CONTEXT+16] - 1)
               @T6[MEMORY_CONTEXT+64] = (@T5[MEMORY_CONTEXT+56] - 1)
               @T6[MEMORY_CONTEXT+64] = (@T6[MEMORY_CONTEXT+64] * 8)
               @T7[MEMORY_CONTEXT+72] = F[MEMORY_CONTEXT+8](@T6[MEMORY_CONTEXT+64])
               @T8[MEMORY_CONTEXT+80] = (I[MEMORY_CONTEXT+16] - 2)
               @T9[MEMORY_CONTEXT+88] = (@T8[MEMORY_CONTEXT+80] - 1)
               @T9[MEMORY_CONTEXT+88] = (@T9[MEMORY_CONTEXT+88] * 8)
               @T10[MEMORY_CONTEXT+96] = F[MEMORY_CONTEXT+8](@T9[MEMORY_CONTEXT+88])
               @T11[MEMORY_CONTEXT+104] = (@T7[MEMORY_CONTEXT+72] + @T10[MEMORY_CONTEXT+96])
               @T12[MEMORY_CONTEXT+112] = (I[MEMORY_CONTEXT+16] - 1)
               @T12[MEMORY_CONTEXT+112] = (@T12[MEMORY_CONTEXT+112] * 8)
               F[MEMORY_CONTEXT+8](@T12[MEMORY_CONTEXT+112]) = @T11[MEMORY_CONTEXT+104]
               I[MEMORY_CONTEXT+16] = (I[MEMORY_CONTEXT+16] + 1)
               JMP L1[PROGRAM_START+288]
               #-- PRINT ((("fibo("+N)+") = ")+F)
L2:            @T13[MEMORY_CONTEXT+120] = (@S2[PROGRAM_END+5] + N[MEMORY_CONTEXT+0])
               @T14[MEMORY_CONTEXT+128] = (@T13[MEMORY_CONTEXT+120] + @S3[PROGRAM_END+11])
               @T15[MEMORY_CONTEXT+136] = (@T14[MEMORY_CONTEXT+128] + F[MEMORY_CONTEXT+8])
               PRINT @T15[MEMORY_CONTEXT+136]
               PRINT @S1[PROGRAM_END+3]
               #-- END
               EXIT
