package se.lnu.os.ht23.a2.required;

import se.lnu.os.ht23.a2.provided.data.BlockInterval;
import se.lnu.os.ht23.a2.provided.data.StrategyType;
import se.lnu.os.ht23.a2.provided.abstract_.Instruction;
import se.lnu.os.ht23.a2.provided.exceptions.InstructionException;
import se.lnu.os.ht23.a2.provided.instructions.AllocationInstruction;
import se.lnu.os.ht23.a2.provided.instructions.CompactInstruction;
import se.lnu.os.ht23.a2.provided.instructions.DeallocationInstruction;
import se.lnu.os.ht23.a2.provided.interfaces.Memory;
import se.lnu.os.ht23.a2.provided.interfaces.SimulationInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SimulationInstanceImpl implements SimulationInstance {
    private Queue<Instruction> remainingInstructions;
    private final MemoryImpl memory;
    private final StrategyType strategyType;
    private List<InstructionException> instructionExceptions;

    public SimulationInstanceImpl(Queue<Instruction> instructions, MemoryImpl memory, StrategyType strategyType){
        this.remainingInstructions = instructions;
        this.memory = memory;
        this.strategyType = strategyType;
        this.instructionExceptions = new ArrayList<>();
    }

    @Override
    public void runAll() {
        this.run(remainingInstructions.size());
    }

    @Override
    public void run(int steps) {
        Instruction i;
        while (steps > 0) {
            i = remainingInstructions.poll();
            if (i.getClass() == CompactInstruction.class) {
                memory.compact();
            } else if (i.getClass() == AllocationInstruction.class) {
                executeAllocation((AllocationInstruction) i);
            } else if (i.getClass() == DeallocationInstruction.class) {
                executeDeallocation((DeallocationInstruction) i);
            }

            steps --;
        }
    }

    @Override
    public Memory getMemory() {
        return this.memory;
    }

    @Override
    public Queue<Instruction> getInstructions() {
        return this.remainingInstructions;
    }

    @Override
    public StrategyType getStrategyType() {
        return this.strategyType;
    }

    @Override
    public List<InstructionException> getExceptions() {
        return this.instructionExceptions;
    }

    private void executeAllocation(AllocationInstruction i) {
        if (!(memory.allocate(this.getStrategyType(), i.getBlockId(), i.getDimension()))) {
            instructionExceptions.add(new InstructionException(i, getFreeSpace()));
        }
    }

    private void executeDeallocation(DeallocationInstruction i) {
        int blockId = i.getBlockId();
        if (!(memory.deallocate(blockId))) {
            instructionExceptions.add(new InstructionException(i, getFreeSpace()));
        }
    }

    private int getFreeSpace() {
        Set<BlockInterval> freespace = memory.freeSlots();
            int space = 0;
            for (BlockInterval b : freespace) {
                space += b.getHighAddress() - b.getLowAddress() + 1;
            }
        return space;
    }

    @Override
    public String toString() {
        return "Simulation Details:\n" +
                "Strategy: " + strategyType + "\n" +
                "List of Remaining Instructions: " + remainingInstructions + "\n" +
                "Current Memory Structure:\n\n" + memory + "\n" +
                "List of Occurred Exceptions: " + instructionExceptions;
    }
}
