def add(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] + registers[source2]

def addi(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    if immediate > 2047:
        immediate = immediate - 4096
    registers[destination] = registers[source1] + immediate

def andFunc(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] & registers[source2]

def andi(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    if immediate > 2047:
        immediate = immediate - 4096
    registers[destination] = registers[source1] & immediate

def auipc(registers, rd, imm):
    destination = int(rd, 2)
    immediate = int(imm, 2)
    registers[destination] = registers[32] + (immediate << 12)

def beq(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if immediate > 4095:
        immediate = immediate - 8192
    if registers[source1] == registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def bge(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if immediate > 4095:
        immediate = immediate - 8192
    if registers[source1] >= registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def bgeu(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if registers[source1] >= registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def blt(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if immediate > 4095:
        immediate = immediate - 8192
    if registers[source1] < registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def bltu(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if registers[source1] < registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def bne(registers, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    if immediate > 4095:
        immediate = immediate - 8192
    if registers[source1] != registers[source2]:
        registers[32] = registers[32] + immediate
    else:
        registers[32] = registers[32] + 4

def jal(registers, rd, imm):
    destination = int(rd, 2)
    immediate = int(imm, 2)
    # UJ format imm's are signed (-1048576 to 1048575)
    if immediate > 1048575:
        immediate = immediate - 2097152
    registers[destination] = registers[32] + 4
    registers[32] = registers[32] + immediate

def jalr(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    # I format imm's are signed (-2048 to 2047)
    if immediate > 2047:
        immediate = immediate - 4096
    registers[destination] = registers[32] + 4
    registers[32] = registers[source1] + immediate

def lb(registers, memory, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    result = memory[memLocation]
    # Only use 8 bits (one byte) in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    registers[destination] = int(bitResult, 2)

def lbu(registers, memory, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    result = memory[memLocation]
    # Only use 8 bits (one byte) in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    registers[destination] = int(bitResult, 2)

def lh(registers, memory, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    lsResult = memory[memLocation]
    msResult = memory[memLocation + 1]
    # Only use 16 bits (two bytes) in this instruction.
    combinedResult = msResult << 8
    combinedResult = combinedResult + lsResult
    bitResult = bin(combinedResult)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    registers[destination] = int(bitResult, 2)

def lhu(registers, memory, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    lsResult = memory[memLocation]
    msResult = memory[memLocation + 1]
    # Only use 16 bits (two bytes) in this instruction.
    combinedResult = msResult << 8
    combinedResult = combinedResult + lsResult
    bitResult = bin(combinedResult)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    registers[destination] = int(bitResult, 2)

def lui(registers, rd, imm):
    destination = int(rd, 2)
    immediate = int(imm, 2)
    immediate = immediate << 12
    registers[destination] = immediate

def lw(registers, memory, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    lsResult = memory[memLocation]
    ssResult = memory[memLocation + 1]
    tsResult = memory[memLocation + 2]
    msResult = memory[memLocation + 3]
    # Use 32 bits (four bytes) in this instruction.
    combinedResult = msResult << 8
    combinedResult = combinedResult + tsResult
    combinedResult = combinedResult << 8
    combinedResult = combinedResult + ssResult
    combinedResult = combinedResult << 8
    combinedResult = combinedResult + lsResult
    bitResult = bin(combinedResult)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    registers[destination] = int(bitResult, 2)

def orFunc(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] | registers[source2]

def ori(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    registers[destination] = registers[source1] | immediate

def sb(registers, memory, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    result = registers[source2]
    bitResult = bin(result)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    memory[memLocation] = int(bitResult[24:], 2)

def sh(registers, memory, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    result = registers[source2]
    bitResult = bin(result)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    memory[memLocation] = int(bitResult[24:], 2)
    memory[memLocation + 1] = int(bitResult[16:24], 2)

def sll(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] << registers[source2]

def slli(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    registers[destination] = registers[source1] << immediate

def slt(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    if registers[source1] < registers[source2]:
        registers[destination] = 1
    else:
        registers[destination] = 0

def slti(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    if registers[source1] < immediate:
        registers[destination] = 1
    else:
        registers[destination] = 0

def sltiu(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    if registers[source1] < immediate:
        registers[destination] = 1
    else:
        registers[destination] = 0

def sltu(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    if registers[source1] < registers[source2]:
        registers[destination] = 1
    else:
        registers[destination] = 0

def sra(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] >> registers[source2]

def srai(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    registers[destination] = registers[source1] >> immediate

def srl(registers, rd, rs1, rs2):
    # From what I read, Python only has an arithmetic right shift.
    # So this one gets to be more *fun*.
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    i = 0
    temp = registers[source1]
    while i < registers[source2]:
        temp = temp // 2
        i += 1
    registers[destination] = temp

def srli(registers, rd, rs1, imm):
    # From what I read, Python only has an arithmetic right shift.
    # So this one gets to be more *fun*.
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    i = 0
    temp = registers[source1]
    while i < immediate:
        temp = temp // 2
        i += 1
    registers[destination] = temp

def sub(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] - registers[source2]

def sw(registers, memory, rs1, rs2, imm):
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    immediate = int(imm, 2)
    memLocation = registers[source1] + immediate
    result = registers[source2]
    bitResult = bin(result)[2:]
    while len(bitResult) < 32:
        bitResult = '0' + bitResult
    memory[memLocation] = int(bitResult[24:], 2)
    memory[memLocation + 1] = int(bitResult[16:24], 2)
    memory[memLocation + 2] = int(bitResult[8:16], 2)
    memory[memLocation + 3] = int(bitResult[:8], 2)

def xor(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] ^ registers[source2]

def xori(registers, rd, rs1, imm):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    immediate = int(imm, 2)
    registers[destination] = registers[source1] ^ immediate

def mul(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    result = registers[source1] * registers[source2]
    # Only use lower 32 bits in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 64:
        bitResult = '0' + bitResult
    lowerBits = bitResult[32:]
    registers[destination] = int(lowerBits, 2)

def mulh(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    result = registers[source1] * registers[source2]
    # Only use upper 32 bits in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 64:
        bitResult = '0' + bitResult
    upperBits = bitResult[:32]
    registers[destination] = int(upperBits, 2)

def mulhsu(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    result = registers[source1] * registers[source2]
    # Only use upper 32 bits in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 64:
        bitResult = '0' + bitResult
    upperBits = bitResult[:32]
    registers[destination] = int(upperBits, 2)

def mulhu(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    result = registers[source1] * registers[source2]
    # Only use upper 32 bits in this instruction.
    bitResult = bin(result)[2:]
    while len(bitResult) < 64:
        bitResult = '0' + bitResult
    upperBits = bitResult[:32]
    registers[destination] = int(upperBits, 2)

def div(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] // registers[source2]

def divu(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] // registers[source2]

def rem(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] % registers[source2]

def remu(registers, rd, rs1, rs2):
    destination = int(rd, 2)
    source1 = int(rs1, 2)
    source2 = int(rs2, 2)
    registers[destination] = registers[source1] % registers[source2]