import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Class to hold instruction and the immediate value or memory address next to it.
 */
class InstructionAndValue{

    // Instruction like "STORE"
    final String instruction;

    // Immediate value or memory address next to instruction.
    // If the whole instruction is "STORE 212", value is 212
    final Integer value;


    public InstructionAndValue(String instruction, int value){
        this.instruction = instruction;
        this.value = value;
    }

    public InstructionAndValue(String instruction){
        this.instruction = instruction;
        this.value = null;
    }

    @Override
    public String toString() {
        return instruction + " " + value.toString();
    }
}


//! Main class
public class App {
    public static void main(String[] args) throws Exception {


        HashMap <Integer, InstructionAndValue> instructionsAndValues = new HashMap <> ();
        // The outer HashMap<Integer, .........> is that; Integer holds the instruction's index
        // Index will be used for jumping and etc.

        // The InstructionAndValue is that; String instruction holds instructions like "LOADM"
        // and the int value holds next int to LOADM.

        // For example if instruction is LOADM 200, the instructionsAndValues will be

        //    {
        //       0: LOADM 200,
        //    }

        // if the next instruction is STORE 202, the instructionsAndValues will be;

        //    {
        //       0: LOADM 200,
        //       1: STORE 202,
        //    }

        // It goes like that, reads all instructions given by you.

        

        // Reading file that contains instructions
        try {

            File file = new File(args[0]);

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {


                // Take the line, split it because maybe instruction is two piece like "STORE 201" 
                String[] rawInstruction = scanner.nextLine().split(" ");



                // The first part of rawInstruction is a String like "START", "ADD", "LOAD" etc.
                String instruction = rawInstruction[0];


                // The second part of rawInstruction is Integer which is next to String instruction.
                // For example if rawInstruction is "LOADM 202", instruction is "LOADM" and integerComingWithInstruction = 202.
                // Another example if rawInstruction is "ADD 5", instrucion is "ADD" and integerComingWithInstruction is 5.
                Integer integerComingWithInstruction;


                try {

                    // Try parsing the second part of instruction as Integer, if catch error,
                    // it means this instruction has no immediate value or memory address.
                    integerComingWithInstruction = Integer.parseInt(rawInstruction[1]);

                } catch (Exception e) {

                    // The instruction has not an int value coming with it like "START", "HALT" or "DISP"
                    integerComingWithInstruction = null;
                    
                }



                InstructionAndValue instructionAndValue;

                try{

                    instructionAndValue = new InstructionAndValue(instruction, integerComingWithInstruction);              
                
                }catch (Exception e){
                   
                    instructionAndValue = new InstructionAndValue(instruction);
               
                }



                // Hashmap's own size is used for auto incrementing, since these adding instruction operation in a while loop,
                // whenever it puts a new instruction to the map, size will be incrementing by 1 autimatically.
                // And this auto-incremented size will our instructions' index.
                // For example at the beginning, size is 0 and let first instruction be "START"
                // There fore it will be {0=START}, and now the size of hashMap is 1, therefore
                // if second instruction is "LOAD 200", it will be {0=START, 1=LOAD 200} and it goes like that.
                // These indexes will be used for jumping etc.
                Integer lineAddressOfInstruction = instructionsAndValues.size();


                instructionsAndValues.put(lineAddressOfInstruction, instructionAndValue);
                

            } // End of while, file is read and instructions are put the hashmap.

            scanner.close();

        } catch (FileNotFoundException e) {

            System.out.println("File Not Found");
            
            e.printStackTrace();
        }



        //! Starting operations.

        // Program Counter
        int PC = 0;

        // Accumulator
        int AC = 0;

        // Flag
        int F = 0;

        // 256 Byte Memory (RAM)
        int[] M = new int[256];

    

        // Execute operations constantly
        while (true) {

            // The current Instruction shown by PC
            InstructionAndValue currentInstruction = instructionsAndValues.get(PC);

            switch(currentInstruction.instruction){
                
                case "START":
                // If the instruction is "START" increment PC by 1.
                    
                    PC++;

                    break;

                
                
                case "LOAD":
                // If instruction is LOAD, take the immediate value coming with "LOAD" and put it to AC.
                    

                    // currentInstruction.value will be immediate value in this case.
                    // For example if instruction is "LOAD 50", value will be 50.


                    // Load 50 to AC.
                    AC = currentInstruction.value;


                    //Go to next line.
                    PC++;

                    break;


                
                case "LOADM":
                // If instruction is "LOADM", load value from memory address to AC.

                    // currentInstruction.value will be memory address in this case
                    // For example if instruction is "LOADM 201", memory address will be 201.


                    // Value from Memory's 201th address and load it to AC.
                    AC = M[currentInstruction.value];


                    // Go to next line.
                    PC++;

                    break;

                
                
                case "STORE":
                // If instruction is "STORE", Store the current AC value to memory.


                    // currentInstruction.value will be memory address in this case
                    // Memory address coming with "STORE", for example if instruction is "LOADM 205", 
                    // memory address will be 205.


                    // Put current AC value to Memory's 205th address.
                    M[currentInstruction.value] = AC;


                    // Go to next line.
                    PC++;

                    break;


                
                case "CMPM":
                // If instruction is "CMPM";
                // If the value in AC is greater than the value in Memory set F flag to 1.
                // If the value in AC is less than the value in Memory set F flag to -1.
                // If the value in AC is equal to the value in Memory set F flag to 0.


                    // currentInstruction.value will be memory address in this case
                    // For example, if instruction is "CMPM 156", 
                    // memory address will be 156.


                    // Value of Memory's 156th address
                    int valueAtMemoryAddress = M[currentInstruction.value];


                    if (AC > valueAtMemoryAddress) {

                        F = 1;

                    } else if (AC < valueAtMemoryAddress) {

                        F = -1;

                    } else {

                        F = 0;

                    }


                    // Go to next line.
                    PC++;

                    break;



                case "CJMP":
                // If the instruction is "CJMP";
                // If the F flag value is positive, update PC with the value coming with "CJMP"

                    // If the F flag value is positive
                    if (F > 0) {

                    // currentInstruction.value is new instruction's line address in this case.
                    int instructionToJump = currentInstruction.value;


                    // Update program counter with it.
                    PC = instructionToJump;

                    break;
                    }


                    // Else go to next line as usual.
                    PC++;
                    
                    break;



                case "JMP":
                // If the instruction is "JMP", update PC with the value coming with "JMP"

                    // currentInstruction.value is new instruction's line address in this case.
                    int instructionToJump = currentInstruction.value;


                    // Update program counter with it.
                    PC = instructionToJump;

                    break;



                case "ADD":
                // If the instruction is "ADD", add immediate value to AC.


                    // currentInstruction.value will be immediate value in this case.
                    // For example, if the instruction is ADD 5, value is 5.
                    AC = AC + currentInstruction.value;

                
                    // Go to next line.
                    PC++;

                    break;



                case "ADDM":
                // If the instruction is "ADDM", add memory value to AC.


                    // currentInstruction.value will be memory address in this case
                    // For example, if the instruction is ADDM 105, memory address is 105.
                    AC = AC + M[currentInstruction.value];


                    // Go to next line.
                    PC++;

                    break;


                case "SUBM":
                // If the instruction is "SUBM", substract memory value from AC. 

                    
                    // currentInstruction.value will be memory address in this case
                    // For example, if the instruction is SUBM 103, memory address is 103.
                    AC = AC - M[currentInstruction.value];


                    // Go to next line.
                    PC++;

                    break;



                case "SUB":
                // If the instruction is "SUB", substract immediate value from AC.

                    
                    // currentInstruction.value will be immediate value in this case
                    // For example, if the instruction is SUB 10, value is 10.
                    AC = AC - currentInstruction.value;


                    // Go to next line.
                    PC++;

                    break;
            


                case "MUL":
                // If the instruction is "MUL", multiply AC with immediate value.

                    
                    // currentInstruction.value will be immediate value in this case
                    // For example, if the instruction is MUL 3, value is 3.
                    AC = AC * currentInstruction.value;


                    // Go to next line.
                    PC++;

                    break;



                case "MULM":
                // If the instruction is "MULM", multiply AC with Memory value.


                    // currentInstruction.value will be memory address in this case
                    // For example, if the instruction is MULM 128, memory address is 128.
                    AC = AC * M[currentInstruction.value];


                    // Go to the next line.
                    PC++;

                    break;



                case "DISP":
                // If the instruction is "DISP", display the value in AC.
                    
                    System.out.println(AC);

                    PC++;

                    break;


                
                case "HALT":
                // If instruction is "HALT". Finish executing.

                    System.exit(1);
                    break;



                default: 
                    throw new Exception("Unknown instruction!");
            
            }
          
        }

    }
}