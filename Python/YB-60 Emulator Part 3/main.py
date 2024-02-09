import sys, string
import Instructions

# Mandatory Variables
# Labels for when you run the program.
labels = "    PC      OPC   INST  rd    rs1  rs2/imm"
# String containing the EBREAK instruction for comparison
EBREAK = "00100073"
# 33 Registers + 1 MB of memory
# index 0 -> x0, index 1 -> x1, ... , index 31 -> x31, index 32 -> pc
registers = [0] * 33
memory = bytearray(1048576)
# The memory will start as all 0's, and may have data loaded into it if
# an object file was provided in the command line.


# Clear Registers before Running Program
def clearRegisters():
    i = 0
    # Set all registers, including PC, to 0.
    while i < 33:
        registers[i] = 0
        i += 1
    # Go back and set SP/x2 to FFFFF
    registers[2] = 1048575


# Load a Program
def loadProgram(objectFile):
    # We need to know the current address and extended address to read
    # the lines properly.
    currentAddress = int('0', 16)
    currentExtend = int('0', 16)

    # We want a loop that runs until we find the EOF command.
    # For this case, I'll use an infinite loop with a break when
    # EOF is detected.
    while True:
        # Read the next line in the file.
        currentLine = objectFile.readline()

        # Check if the line is valid. (Is there a : at the beginning of the line?)
        currentLine = currentLine.lstrip()
        firstChar = currentLine[:1]
        # If this check fails, we skip the invalid line.
        if firstChar == ':':
            # We can continue processing the line.
            # First, trim the : and \n from the currentLine.
            currentLine = currentLine.lstrip(':').rstrip("\n")

            # Next, we need to validate the line's checksum. If a line's
            # checksum is invalid, we exit the program.
            if checksum(currentLine) == False:
                print("Format error input file: " + sys.argv[1])
                sys.exit()

            # The Record Type (00, 01, 02) is always the 4th byte after the :.
            # Begin by checking what Record Type we have.
            recordType = currentLine[6:8]

            # Type 01 indicates EOF, so break in that instance.
            # Type 02 indicates extended address, so set the offset in that case.
            # Type 00 indicates data, so read the data into memory.
            if recordType == "01":
                break
            elif recordType == "02":
                # The extended address will be in the 5th and 6th byte after the :.
                currentExtend = int(currentLine[8:12], 16)
            elif recordType != "00":
                # At this point, anything that isn't 01, 02, or 00, is invalid.
                # Exit the program if this happens.
                print("Format error input file: " + sys.argv[1])
                sys.exit()
            else:
                # The number of data bytes is the first byte, and the
                # address is in the 2nd and 3rd bytes.
                # Get these values first, then trim them off the data,
                # along with the record type, leaving just data and checksum.
                numberOfBytes = int(currentLine[:2], 16)
                currentAddress = int(currentLine[2:6], 16)
                dataBytes = currentLine[8:]

                # Additionally, combine currentAddress and currentExtend
                # to get the full address location.
                addressIndex = (currentExtend * 16) + currentAddress

                # Now, put the bytes into memory.
                while numberOfBytes != 0:
                    # Get the first byte in the data.
                    firstByte = int(dataBytes[:2], 16)
                    dataBytes = dataBytes[2:]

                    # Store the byte in memory.
                    memory[addressIndex] = firstByte

                    # Decrement numberOfBytes and update the currentAddress
                    numberOfBytes -= 1
                    addressIndex += 1


# Compute the currentLine Checksum
def checksum(currentLine):
    # Separate the checksum byte from the rest of the line.
    givenChecksum = int(currentLine[len(currentLine) - 2:], 16)
    dataBytes = currentLine[:len(currentLine) - 2]

    # A couple variables to use in calculating the checksum.
    dataSum = 0
    calculatedChecksum = 0
    byteBuffer = bytearray(1)

    # While there are still dataBytes, take a byte and
    # add it to the dataSum.
    while dataBytes:
        buffer = int(dataBytes[:2], 16)
        dataSum += buffer
        dataBytes = dataBytes[2:]

    # Turn the dataSum into a hex string, and remove the "0x".
    # Then, take only the last byte of the string and turn it
    # back into a hex int.
    hexBuffer = str(hex(dataSum))
    hexBuffer = hexBuffer[2:]
    hexBuffer = hexBuffer[len(hexBuffer) - 2:]
    calculatedChecksum = int(hexBuffer, 16)

    # Now, perform the 2's Compliment on the calculated checksum.
    byteBuffer[0] = calculatedChecksum
    temp = byteBuffer[0] - (1 << 8)
    calculatedChecksum = temp * -1

    # Finally, compare the given and calculated Checksums
    # to see if the currentLine has been changed or not.
    if calculatedChecksum == givenChecksum:
        return True
    else:
        return False


# Extract Data from R Format Instruction
def RFormat(bitString):
    rd = bitString[20:25]
    rs1 = bitString[12:17]
    rs2 = bitString[7:12]
    return rd, rs1, rs2


# Extract Data from I Format Instruction
def IFormat(bitString):
    rd = bitString[20:25]
    rs1 = bitString[12:17]
    imm = bitString[:12]
    return rd, rs1, imm


# Extract Data from S Format Instruction
def SFormat(bitString):
    rs1 = bitString[12:17]
    rs2 = bitString[7:12]
    imm = bitString[:7] + bitString[20:25]
    return rs1, rs2, imm


# Extract Data from SB Format Instruction
def SBFormat(bitString):
    rs1 = bitString[12:17]
    rs2 = bitString[7:12]
    imm = bitString[:1] + bitString[24:25] + bitString[1:7] + bitString[20:24] + '0'
    return rs1, rs2, imm


# Extract Data from U Format Instruction
def UFormat(bitString):
    rd = bitString[20:25]
    imm = bitString[:20]
    return rd, imm


# Extract Data from UJ Format Instruction
def UJFormat(bitString):
    rd = bitString[20:25]
    imm = bitString[:1] + bitString[12:20] + bitString[11:12] + bitString[1:11] + '0'
    return rd, imm


# Determine which Load Instruction is in use
def determineLoad(bitString, rd, rs1, imm):
    func3 = bitString[17:20]
    if func3 == '000':
        Instructions.lb(registers, memory, rd, rs1, imm)
        return 'LB'
    elif func3 == '001':
        Instructions.lh(registers, memory, rd, rs1, imm)
        return 'LH'
    elif func3 == '010':
        Instructions.lw(registers, memory, rd, rs1, imm)
        return 'LW'
    elif func3 == '100':
        Instructions.lbu(registers, memory, rd, rs1, imm)
        return 'LBU'
    elif func3 == '101':
        Instructions.lhu(registers, memory, rd, rs1, imm)
        return 'LHU'


# Determine which Store Instruction is in use
def determineStore(bitString, rs1, rs2, imm):
    func3 = bitString[17:20]
    if func3 == '000':
        Instructions.sb(registers, memory, rs1, rs2, imm)
        return 'SB'
    elif func3 == '001':
        Instructions.sh(registers, memory, rs1, rs2, imm)
        return 'SH'
    elif func3 == '010':
        Instructions.sw(registers, memory, rs1, rs2, imm)
        return 'SW'


# Determine which Branch Instruction is in use
def determineBranch(bitString, rs1, rs2, imm):
    func3 = bitString[17:20]
    if func3 == '000':
        Instructions.beq(registers, rs1, rs2, imm)
        return 'BEQ'
    elif func3 == '001':
        Instructions.bne(registers, rs1, rs2, imm)
        return 'BNE'
    elif func3 == '100':
        Instructions.blt(registers, rs1, rs2, imm)
        return 'BLT'
    elif func3 == '101':
        Instructions.bge(registers, rs1, rs2, imm)
        return 'BGE'
    elif func3 == '110':
        Instructions.bltu(registers, rs1, rs2, imm)
        return 'BLTU'
    elif func3 == '111':
        Instructions.bgeu(registers, rs1, rs2, imm)
        return 'BGEU'


# Determine which OP-Imm Instruction is in use
def determineOPImm(bitString, rd, rs1, imm):
    func3 = bitString[17:20]
    func7 = bitString[:7]
    if func3 == '101' and func7 == '0100000':
        Instructions.srai(registers, rd, rs1, imm)
        return 'SRAI'
    elif func3 == '101' and func7 == '0000000':
        Instructions.srli(registers, rd, rs1, imm)
        return 'SRLI'
    elif func3 == '001' and func7 == '0000000':
        Instructions.slli(registers, rd, rs1, imm)
        return 'SLLI'
    elif func3 == '000':
        Instructions.addi(registers, rd, rs1, imm)
        return 'ADDI'
    elif func3 == '010':
        Instructions.slti(registers, rd, rs1, imm)
        return 'SLTI'
    elif func3 == '011':
        Instructions.sltiu(registers, rd, rs1, imm)
        return 'SLTIU'
    elif func3 == '100':
        Instructions.xori(registers, rd, rs1, imm)
        return 'XORI'
    elif func3 == '110':
        Instructions.ori(registers, rd, rs1, imm)
        return 'ORI'
    elif func3 == '111':
        Instructions.andi(registers, rd, rs1, imm)
        return 'ANDI'


# Determine which OP Instruction is in use
def determineOP(bitString, rd, rs1, rs2):
    func3 = bitString[17:20]
    func7 = bitString[:7]
    if func7 == '0000001':
        if func3 == '000':
            Instructions.mul(registers, rd, rs1, rs2)
            return 'MUL'
        elif func3 == '001':
            Instructions.mulh(registers, rd, rs1, rs2)
            return 'MULH'
        elif func3 == '010':
            Instructions.mulhsu(registers, rd, rs1, rs2)
            return 'MULHSU'
        elif func3 == '011':
            Instructions.mulhu(registers, rd, rs1, rs2)
            return 'MULHU'
        elif func3 == '100':
            Instructions.div(registers, rd, rs1, rs2)
            return 'DIV'
        elif func3 == '101':
            Instructions.divu(registers, rd, rs1, rs2)
            return 'DIVU'
        elif func3 == '110':
            Instructions.rem(registers, rd, rs1, rs2)
            return 'REM'
        elif func3 == '111':
            Instructions.remu(registers, rd, rs1, rs2)
            return 'REMU'
    elif func7 == '0000000':
        if func3 == '000':
            Instructions.add(registers, rd, rs1, rs2)
            return 'ADD'
        if func3 == '001':
            Instructions.sll(registers, rd, rs1, rs2)
            return 'SLL'
        if func3 == '010':
            Instructions.slt(registers, rd, rs1, rs2)
            return 'SLT'
        if func3 == '011':
            Instructions.sltu(registers, rd, rs1, rs2)
            return 'SLTU'
        if func3 == '100':
            Instructions.xor(registers, rd, rs1, rs2)
            return 'XOR'
        if func3 == '101':
            Instructions.srl(registers, rd, rs1, rs2)
            return 'SRL'
        if func3 == '110':
            Instructions.orFunc(registers, rd, rs1, rs2)
            return 'OR'
        if func3 == '111':
            Instructions.andFunc(registers, rd, rs1, rs2)
            return 'AND'
    elif func7 == '0100000':
        if func3 == '000':
            Instructions.sub(registers, rd, rs1, rs2)
            return 'SUB'
        if func3 == '101':
            Instructions.sra(registers, rd, rs1, rs2)
            return 'SRA'


# Display a Memory Address
def displayAddress(userInput):
    # Need a String Buffer to ensure the data at the specified address
    # is actually in caps, to match output.
    buffer = str(hex(memory[int(userInput, 16)]))
    buffer = buffer.upper()
    buffer = buffer[2:]

    # Additionally, if the data is a single digit (0-F), we need to
    # add a 0 in front of the digit (00-0F), to match output.
    if len(buffer) != 2:
        hexData = '0' + buffer
    else:
        hexData = buffer

    # Can actually output the memory location and data now.
    print(" " + userInput + "   " + hexData)


# Display a Range of Memory Addresses
def displayAddressRange(userInput):
    # First, separate the input into the start and end address.
    inputTuple = userInput.partition('.')
    startAddress = inputTuple[0]
    endAddress = inputTuple[2]

    # We will be using a loop, and our 'i' in this case is how many
    # addresses we need to print, or end - start.
    # We also want the current address to access memory[], and an
    # iterator to keep track of the number of bytes printed on one line.
    bytesToPrint = int(endAddress, 16) - int(startAddress, 16)
    currentAddress = int(startAddress, 16)
    bytesOnThisLine = 0

    # The outer loop keeps track of how many bytes are left,
    # and the inner loop keeps track of how many bytes are on a line.
    while bytesToPrint > -1:
        # Each time the outer loop, loops, we need to print the line header,
        # which simply has the address of the first byte on the line,
        # which is also our currentAddress.
        buffer = str(hex(currentAddress))
        buffer = buffer.upper()
        hexAddress = buffer[2:]
        currentLine = " " + hexAddress + "   "

        while bytesOnThisLine < 8:
            # The individual data printing is very similar to displayAddress,
            # but that function outputs current address, so I can't just reuse it
            # without a refactor/function decomposition.
            buffer = str(hex(memory[currentAddress]))
            buffer = buffer.upper()
            buffer = buffer[2:]

            # Format single digit data for output (0-F --> 00-0F)
            if len(buffer) != 2:
                hexData = '0' + buffer
            else:
                hexData = buffer

            # If this is not the 8th byte on the line, add the data and a space
            # to the line.  If it is the 8th, just add the data.
            if bytesOnThisLine != 7:
                currentLine = currentLine + hexData + ' '
            else:
                currentLine = currentLine + hexData

            # Increment the bytesOnThisLine and currentAddress,
            # and decrement the bytesToPrint.
            bytesOnThisLine += 1
            currentAddress += 1
            bytesToPrint -= 1

            # If we have printed every byte, break out of the loop now.
            if bytesToPrint == -1:
                break

        # After each line of data is collected in currentLine,
        # print the line and set bytesOnThisLine to 0 for the next loop.
        print(currentLine)
        bytesOnThisLine = 0


# Edit Memory Locations
def editMemory(userInput):
    # To start, let's separate the starting address from the data.
    inputTuple = userInput.partition(': ')
    currentAddress = int(inputTuple[0], 16)
    dataList = inputTuple[2]

    # Next, as long as there is data in the list, we need to change
    # the current byte to the first byte in the list.
    while dataList:
        # Separate the first byte from the rest of the list.
        bufferTuple = dataList.partition(" ")
        firstByte = bufferTuple[0]
        dataList = bufferTuple[2]

        # Change the current addressed byte to the list's byte.
        memory[currentAddress] = int(firstByte, 16)

        # Increment the current address
        currentAddress += 1


# Disassemble Object Code
def disassembleCode(userInput):
    # Return to RISC-V assembly code.
    # Disassembles code from user inputted memory location
    # to EBREAK or end of memory.
    startAddress = (userInput.upper()).rstrip('T')
    currentAddress = int(startAddress, 16)

    # We want to keep reading from the user inputted address
    # until we read an EBREAK instruction, or until the end of the memory.
    # For memory, we can keep track using the while condition.
    # For EBREAK, we will just break out of the loop.
    while currentAddress != 1048576:
        # Get the 4 byte instruction from memory.
        # ls = Least Significant, ss = Second Significant,
        # ts = Third Significant, ms = Most Significant
        lsByte = memory[currentAddress]
        ssByte = memory[currentAddress + 1]
        tsByte = memory[currentAddress + 2]
        msByte = memory[currentAddress + 3]

        # Merge the 4 bytes into the whole instruction.
        opcode = msByte << 8
        opcode = opcode + tsByte
        opcode = opcode << 8
        opcode = opcode + ssByte
        opcode = opcode << 8
        opcode = opcode + lsByte

        # If the instruction equals 1048691, that is an EBREAK.
        if opcode == 1048691:
            print('ebreak')
            break

        # Next, determine the instruction format and read the
        # data for that format.
        bitString = bin(opcode)[2:]
        while len(bitString) < 32:
            bitString = '0' + bitString
        importantOPCbits = bitString[25:30]

        if importantOPCbits == '00000':
            # Load Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineLoad(bitString, rd, rs1, imm)
            inst = inst.lower()
            while len(inst) < 6:
                inst = ' ' + inst
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            rs1 = int(rs1, 2)
            imm = int(imm, 2)
            # I format imm's are signed (-2048 to 2047)
            if imm > 2047:
                imm = imm - 4096
            # Print the instruction's data.
            print(inst + ' x{}, {}(x{})'.format(rd, imm, rs1))
        elif importantOPCbits == '01000':
            # Store Instruction, S format
            rs1, rs2, imm = SFormat(bitString)
            # Determine the Specific Instruction
            inst = determineStore(bitString, rs1, rs2, imm)
            inst = inst.lower()
            while len(inst) < 6:
                inst = ' ' + inst
            # Convert the data to numbers, instead of a binary string.
            rs1 = int(rs1, 2)
            rs2 = int(rs2, 2)
            imm = int(imm, 2)
            # S format imm's are signed (-2048 to 2047)
            if imm > 2047:
                imm = imm - 4096
            # Print the instruction's data.
            print(inst + ' x{}, {}(x{})'.format(rs2, imm, rs1))
        elif importantOPCbits == '11000':
            # Branch Instruction, SB format
            rs1, rs2, imm = SBFormat(bitString)
            # Determine the Specific Instruction
            inst = determineBranch(bitString, rs1, rs2, imm)
            inst = inst.lower()
            while len(inst) < 6:
                inst = ' ' + inst
            # Convert the data to numbers, instead of a binary string.
            rs1 = int(rs1, 2)
            rs2 = int(rs2, 2)
            imm = int(imm, 2)
            # SB format imm's are signed (-4096 to 4095)
            # Except bgeu and bltu.
            if imm > 4095 and (inst != "bgeu" and inst != "bltu"):
                imm = imm - 8192
            # Print the instruction's data.
            print(inst + ' x{}, x{}, {}'.format(rs1, rs2, imm))
        elif importantOPCbits == '11001':
            # Jalr Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            Instructions.jalr(registers, rd, rs1, imm)
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            rs1 = int(rs1, 2)
            imm = int(imm, 2)
            # I format imm's are signed (-2048 to 2047)
            if imm > 2047:
                imm = imm - 4096
            # We know the instruction is JALR.
            # Print the instruction's data.
            print('  jalr x{}, {}(x{})'.format(rd, imm, rs1))
        elif importantOPCbits == '11011':
            # Jal Instruction, UJ format
            rd, imm = UJFormat(bitString)
            Instructions.jal(registers, rd, imm)
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            imm = int(imm, 2)
            # UJ format imm's are signed (-1048576 to 1048575)
            if imm > 1048575:
                imm = imm - 2097152
            # We know the instruction is JAL.
            # Print the instruction's data.
            print('   jal x{}, {}'.format(rd, imm))
        elif importantOPCbits == '00100':
            # OP-Imm Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOPImm(bitString, rd, rs1, imm)
            inst = inst.lower()
            while len(inst) < 6:
                inst = ' ' + inst
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            rs1 = int(rs1, 2)
            imm = int(imm, 2)
            # I format imm's are signed (-2048 to 2047)
            # Except sltiu
            if imm > 2047 and inst != "sltiu":
                imm = imm - 4096
            # Print the instruction's data.
            print(inst + ' x{}, x{}, {}'.format(rd, rs1, imm))
        elif importantOPCbits == '01100':
            # OP Instruction, R format
            rd, rs1, rs2 = RFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOP(bitString, rd, rs1, rs2)
            inst = inst.lower()
            while len(inst) < 6:
                inst = ' ' + inst
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            rs1 = int(rs1, 2)
            rs2 = int(rs2, 2)
            # Print the instruction's data.
            print(inst + ' x{}, x{}, x{}'.format(rd, rs1, rs2))
        elif importantOPCbits == '00101':
            # AUIPC Instruction, U format
            rd, imm = UFormat(bitString)
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            imm = int(imm, 2)
            # U format imm's are signed (-524288 to 524287)
            if imm > 524287:
                imm = imm - 1048576
            # We know the instruction is AUIPC.
            Instructions.auipc(registers, rd, imm)
            # Print the instruction's data.
            print(' auipc x{}, {}'.format(rd, imm))
        elif importantOPCbits == '01101':
            # LUI Instruction, U format
            rd, imm = UFormat(bitString)
            # Convert the data to numbers, instead of a binary string.
            rd = int(rd, 2)
            imm = int(imm, 2)
            # U format imm's are signed (-524288 to 524287)
            if imm > 524287:
                imm = imm - 1048576
            # We know the instruction is LUI.
            Instructions.lui(registers, rd, imm)
            # Print the instruction's data.
            print('   lui x{}, {}'.format(rd, imm))

        currentAddress = currentAddress + 4


# Run a Program
# noinspection PyTypeChecker
def runProgram(userInput):
    # Before running, clear the registers, and get the starting address,
    # extended to 5 digits.
    # We currently do not support actually running the code, but we can
    # interpret the hex values into instructions.
    clearRegisters()
    startAddress = (userInput.upper()).rstrip('R')
    registers[32] = int(startAddress, 16)
    print(labels)

    # We want to keep reading from the user inputted address
    # until we read an EBREAK instruction, or until the end of the memory.
    # For memory, we can keep track using the while condition.
    # For EBREAK, we will just break out of the loop.
    while registers[32] != 1048576:
        # First, calculate the PC value and extend it to 5 characters.
        pcBuffer = str(hex(registers[32]))
        pcBuffer = pcBuffer.upper()
        pcBuffer = pcBuffer[2:]
        while len(pcBuffer) < 5:
            pcBuffer = '0' + pcBuffer
        currentAddress = pcBuffer

        # Next, get the 4 byte instruction from memory.
        # ls = Least Significant, ss = Second Significant,
        # ts = Third Significant, ms = Most Significant
        lsByte = memory[registers[32]]
        ssByte = memory[registers[32] + 1]
        tsByte = memory[registers[32] + 2]
        msByte = memory[registers[32] + 3]

        # Merge the 4 bytes into the whole instruction.
        opcode = msByte << 8
        opcode = opcode + tsByte
        opcode = opcode << 8
        opcode = opcode + ssByte
        opcode = opcode << 8
        opcode = opcode + lsByte

        # The whole instruction is called OPC when output,
        # pad the string while we've got it.
        opcBuffer = str(hex(opcode))
        opcBuffer = opcBuffer.upper()
        opcBuffer = opcBuffer[2:]
        while len(opcBuffer) < 8:
            opcBuffer = '0' + opcBuffer
        opc = opcBuffer

        # If the instruction equals 1048691, that is an EBREAK.
        if opcode == 1048691:
            print(' ' + currentAddress + ' ' + opc + ' EBREAK')
            break

        # Next, determine the instruction format and read the
        # data for that format.
        bitString = bin(opcode)[2:]
        while len(bitString) < 32:
            bitString = '0' + bitString
        importantOPCbits = bitString[25:30]

        if importantOPCbits == '00000':
            # Load Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineLoad(bitString, rd, rs1, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01000':
            # Store Instruction, S format
            rs1, rs2, imm = SFormat(bitString)
            # Determine the Specific Instruction
            inst = determineStore(bitString, rs1, rs2, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + '       ' + rs1 + ' ' + rs2 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '11000':
            # Branch Instruction, SB format
            rs1, rs2, imm = SBFormat(bitString)
            # Determine the Specific Instruction
            inst = determineBranch(bitString, rs1, rs2, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + '       ' + rs1 + ' ' + rs2 + ' ' + imm)
        elif importantOPCbits == '11001':
            # Jalr Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # We know the instruction is JALR.
            Instructions.jalr(registers, rd, rs1, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '   JALR ' + rd + ' ' + rs1 + ' ' + imm)
        elif importantOPCbits == '11011':
            # Jal Instruction, UJ format
            rd, imm = UJFormat(bitString)
            # We know the instruction is JAL.
            Instructions.jal(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '    JAL ' + rd + '       ' + imm)
        elif importantOPCbits == '00100':
            # OP-Imm Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOPImm(bitString, rd, rs1, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01100':
            # OP Instruction, R format
            rd, rs1, rs2 = RFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOP(bitString, rd, rs1, rs2)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + rs2)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '00101':
            # AUIPC Instruction, U format
            rd, imm = UFormat(bitString)
            # We know the instruction is AUIPC.
            Instructions.auipc(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '  AUIPC ' + rd + '       ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01101':
            # LUI Instruction, U format
            rd, imm = UFormat(bitString)
            # We know the instruction is LUI.
            Instructions.lui(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '    LUI ' + rd + '       ' + imm)
            registers[32] = registers[32] + 4


def stepThroughProgram(userInput):
    # Before running, clear the registers, and get the starting address,
    # extended to 5 digits.
    # We currently do not support actually running the code, but we can
    # interpret the hex values into instructions.
    clearRegisters()
    startAddress = (userInput.upper()).rstrip('S')
    registers[32] = int(startAddress, 16)
    print(labels)

    cont = "I"

    # We want to keep reading from the user inputted address
    # until we read an EBREAK instruction, or until the end of the memory.
    # For memory, we can keep track using the while condition.
    # For EBREAK, we will just break out of the loop.
    while registers[32] != 1048576 and cont != "N":
        # First, calculate the PC value and extend it to 5 characters.
        pcBuffer = str(hex(registers[32]))
        pcBuffer = pcBuffer.upper()
        pcBuffer = pcBuffer[2:]
        while len(pcBuffer) < 5:
            pcBuffer = '0' + pcBuffer
        currentAddress = pcBuffer

        # Next, get the 4 byte instruction from memory.
        # ls = Least Significant, ss = Second Significant,
        # ts = Third Significant, ms = Most Significant
        lsByte = memory[registers[32]]
        ssByte = memory[registers[32] + 1]
        tsByte = memory[registers[32] + 2]
        msByte = memory[registers[32] + 3]

        # Merge the 4 bytes into the whole instruction.
        opcode = msByte << 8
        opcode = opcode + tsByte
        opcode = opcode << 8
        opcode = opcode + ssByte
        opcode = opcode << 8
        opcode = opcode + lsByte

        # The whole instruction is called OPC when output,
        # pad the string while we've got it.
        opcBuffer = str(hex(opcode))
        opcBuffer = opcBuffer.upper()
        opcBuffer = opcBuffer[2:]
        while len(opcBuffer) < 8:
            opcBuffer = '0' + opcBuffer
        opc = opcBuffer

        # If the instruction equals 1048691, that is an EBREAK.
        if opcode == 1048691:
            print(' ' + currentAddress + ' ' + opc + ' EBREAK')
            break

        # Next, determine the instruction format and read the
        # data for that format.
        bitString = bin(opcode)[2:]
        while len(bitString) < 32:
            bitString = '0' + bitString
        importantOPCbits = bitString[25:30]

        if importantOPCbits == '00000':
            # Load Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineLoad(bitString, rd, rs1, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01000':
            # Store Instruction, S format
            rs1, rs2, imm = SFormat(bitString)
            # Determine the Specific Instruction
            inst = determineStore(bitString, rs1, rs2, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + '       ' + rs1 + ' ' + rs2 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '11000':
            # Branch Instruction, SB format
            rs1, rs2, imm = SBFormat(bitString)
            # Determine the Specific Instruction
            inst = determineBranch(bitString, rs1, rs2, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + '       ' + rs1 + ' ' + rs2 + ' ' + imm)
        elif importantOPCbits == '11001':
            # Jalr Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # We know the instruction is JALR.
            Instructions.jalr(registers, rd, rs1, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '   JALR ' + rd + ' ' + rs1 + ' ' + imm)
        elif importantOPCbits == '11011':
            # Jal Instruction, UJ format
            rd, imm = UJFormat(bitString)
            # We know the instruction is JAL.
            Instructions.jal(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '    JAL ' + rd + '       ' + imm)
        elif importantOPCbits == '00100':
            # OP-Imm Instruction, I format
            rd, rs1, imm = IFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOPImm(bitString, rd, rs1, imm)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01100':
            # OP Instruction, R format
            rd, rs1, rs2 = RFormat(bitString)
            # Determine the Specific Instruction
            inst = determineOP(bitString, rd, rs1, rs2)
            while len(inst) < 7:
                inst = ' ' + inst
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + inst + ' ' + rd + ' ' + rs1 + ' ' + rs2)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '00101':
            # AUIPC Instruction, U format
            rd, imm = UFormat(bitString)
            # We know the instruction is AUIPC.
            Instructions.auipc(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '  AUIPC ' + rd + '       ' + imm)
            registers[32] = registers[32] + 4
        elif importantOPCbits == '01101':
            # LUI Instruction, U format
            rd, imm = UFormat(bitString)
            # We know the instruction is LUI.
            Instructions.lui(registers, rd, imm)
            # Print the instruction's data.
            print(' ' + currentAddress + ' ' + opc + '    LUI ' + rd + '       ' + imm)
            registers[32] = registers[32] + 4
        # Ask for continue
        cont = "I"
        while cont == "I":
            cont = input("Commands:   Y: Continue   I: Show Register Info   N: Stop   :> ")
            cont = cont.upper()
            if cont == "I":
                printRegisters()


# Display Register Contents
def printRegisters():
    # Yeah it just prints the registers contents in hex.
    # Not so long and bad anymore, now that I use an array for registers
    i = 0
    while i < 32:
        currentRegister = hex(registers[i])
        currentRegister = currentRegister[2:]
        while len(currentRegister) < 8:
            currentRegister = '0' + currentRegister
        output = "x" + str(i) + " " + currentRegister
        while len(output) < 12:
            output = ' ' + output
        print(output.upper())
        i += 1


# Main
def main():
    # First off, we must determine if the user has provided an object file
    # or not.  If we have 2 arguments, we were provided a file, and need to
    # read in the data to memory.
    if len(sys.argv) == 2:
        objectFile = open(sys.argv[1], 'r')
        loadProgram(objectFile)
        objectFile.close()

    # Now that the basic setup is complete, enter the main program loop
    while True:
        userInput = input("> ")

        # Determine which option was selected based on input.
        if userInput == "exit":
            # Function 8: Exit the Monitor
            sys.exit()
        elif userInput == "info":
            # Function 7: Display Register Contents
            printRegisters()
        elif all(c in string.hexdigits for c in userInput):
            # Function 1: Display a Memory Address
            displayAddress(userInput)
        elif userInput.find('.') != -1:
            # Function 2: Display a Range of Memory Addresses
            displayAddressRange(userInput)
        elif userInput.find(':') != -1:
            # Function 3: Edit Memory Locations
            editMemory(userInput)
        elif (userInput.upper()).find('T') != -1:
            # Function 4: Disassemble Object Code
            disassembleCode(userInput)
        elif (userInput.upper()).find('R') != -1:
            # Function 5: Run a Program
            runProgram(userInput)
        elif (userInput.upper()).find('S') != -1:
            # Function 6: Step Through Instructions
            stepThroughProgram(userInput)
        else:
            # Unidentified Command
            print("Error: Unidentified Command")


main()
