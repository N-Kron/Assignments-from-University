package se.lnu.os.ht23.a2;

import org.junit.jupiter.api.Test;
import se.lnu.os.ht23.a2.provided.abstract_.Instruction;
import se.lnu.os.ht23.a2.provided.data.BlockInterval;
import se.lnu.os.ht23.a2.provided.data.StrategyType;
import se.lnu.os.ht23.a2.provided.instructions.AllocationInstruction;
import se.lnu.os.ht23.a2.provided.instructions.CompactInstruction;
import se.lnu.os.ht23.a2.provided.instructions.DeallocationInstruction;
import se.lnu.os.ht23.a2.provided.interfaces.SimulationInstance;
import se.lnu.os.ht23.a2.required.MemoryImpl;
import se.lnu.os.ht23.a2.required.SimulationInstanceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTests {
    @Test // Should be the only one working before starting your implementation
    void dummyTest() {
        SimulationInstance sim = new SimulationInstanceImpl(
                new ArrayDeque<>(),
                new MemoryImpl(10),
                StrategyType.BEST_FIT);
        sim.runAll();
        assertTrue(sim.getExceptions().isEmpty());
        assertNotEquals(StrategyType.WORST_FIT, sim.getStrategyType());
        System.out.println(sim);
    }

    @Test
    void oneInstructionTest() {
        Queue<Instruction> instr = new ArrayDeque<>();
        instr.add(new CompactInstruction());
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                new MemoryImpl(10),
                StrategyType.BEST_FIT);
        assertEquals(1, sim.getInstructions().size());
        assertInstanceOf(CompactInstruction.class, sim.getInstructions().peek());
        sim.runAll();
        assertEquals(0, sim.getInstructions().size());
        assertNull(sim.getInstructions().peek());
    }

    @Test
    void twoInstructionsTest() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new DeallocationInstruction(100),
                new AllocationInstruction(1,5)
        ));
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                new MemoryImpl(10),
                StrategyType.FIRST_FIT);
        assertEquals(2, sim.getInstructions().size());
        assertInstanceOf(DeallocationInstruction.class, sim.getInstructions().peek());
        assertEquals(100, ((DeallocationInstruction) Objects.requireNonNull(sim.getInstructions().peek())).getBlockId());
        sim.run(1);
        assertEquals(1, sim.getInstructions().size());
        assertEquals(1, sim.getExceptions().size());
        assertEquals(10, sim.getExceptions().get(0).getAllocatableMemoryAtException());
        assertEquals(DeallocationInstruction.class, sim.getExceptions().get(0).getInstructionType());
        assertInstanceOf(AllocationInstruction.class, sim.getInstructions().peek());
        assertEquals(1, ((AllocationInstruction) Objects.requireNonNull(sim.getInstructions().peek())).getBlockId());
        assertEquals(5, ((AllocationInstruction) Objects.requireNonNull(sim.getInstructions().peek())).getDimension());
        sim.runAll();
        assertEquals(0, sim.getInstructions().size());
        assertNull(sim.getInstructions().peek());
        assertFalse(sim.getMemory().containsBlock(2));
        assertEquals(5, sim.getMemory().blockDimension(1));
        assertEquals(0, sim.getMemory().getBlockInterval(1).getLowAddress());
        assertEquals(4, sim.getMemory().getBlockInterval(1).getHighAddress());
        assertTrue(sim.getMemory().neighboringBlocks(1).isEmpty());
        assertEquals(1, sim.getMemory().freeSlots().size());
        assertTrue(sim.getMemory().freeSlots().contains(new BlockInterval(5, 9)));
        assertEquals(0, sim.getMemory().fragmentation());
    }

    @Test
    void testAllocation() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(-1,2),
                new AllocationInstruction(0,2),
                new AllocationInstruction(1, 10),
                new AllocationInstruction(2, 1)
        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.runAll();
        assertEquals(3, sim.getExceptions().size());
        assertEquals(0, sim.getMemory().getBlockInterval(1).getLowAddress());
        assertEquals(9, sim.getMemory().getBlockInterval(1).getHighAddress());
    }

    @Test
    void testDeallocation() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1, 1),
                new AllocationInstruction(2, 3),
                new AllocationInstruction(6, 2),
                new AllocationInstruction(7, 4),
                new AllocationInstruction(3, 2),
                new AllocationInstruction(4, 3),
                new AllocationInstruction(5, 2),
                new DeallocationInstruction(1),
                new DeallocationInstruction(3),
                new DeallocationInstruction(5),
                new DeallocationInstruction(6),
                new DeallocationInstruction(1)
        ));

        MemoryImpl mem = new MemoryImpl(17);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.run(12);
        mem.printMemory();
        assertEquals(1, sim.getExceptions().size());
    }

    @Test
    void testCompact() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1,1),
                new AllocationInstruction(2,2),
                new AllocationInstruction(3,1),
                new AllocationInstruction(4,1),
                new AllocationInstruction(5,1),
                new AllocationInstruction(6,1),
                new AllocationInstruction(7,2),
                new AllocationInstruction(9,1),
                new DeallocationInstruction(2),
                new DeallocationInstruction(4),
                new CompactInstruction()
        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.runAll();
        mem.printMemory();
        assertEquals(0, sim.getExceptions().size());
        assertEquals(2, sim.getMemory().neighboringBlocks(6).size());
        assertTrue(sim.getMemory().neighboringBlocks(6).contains(5));
        assertTrue(sim.getMemory().neighboringBlocks(6).contains(7));
    }

    @Test
    void testBestFit() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1,2),
                new AllocationInstruction(2,4),
                new AllocationInstruction(3,1),
                new DeallocationInstruction(2),
                new AllocationInstruction(4, 2),
                new DeallocationInstruction(4),
                new AllocationInstruction(5, 4)
        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.BEST_FIT);

        sim.run(5);
        assertEquals(0, sim.getExceptions().size());
        assertEquals(7, sim.getMemory().getBlockInterval(4).getLowAddress());
        assertEquals(8, sim.getMemory().getBlockInterval(4).getHighAddress());
        mem.printMemory();
        sim.run(2);
        assertEquals(0, sim.getExceptions().size());
        assertEquals(2, sim.getMemory().getBlockInterval(5).getLowAddress());
        assertEquals(5, sim.getMemory().getBlockInterval(5).getHighAddress());
    }

    @Test
    void testWorstFit() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1,2),
                new AllocationInstruction(2,2),
                new AllocationInstruction(3,1),
                new DeallocationInstruction(2),
                new AllocationInstruction(4, 2),
                new AllocationInstruction(5, 1),
                new AllocationInstruction(6, 2)
        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.WORST_FIT);

        sim.run(5);
        assertEquals(0, sim.getExceptions().size());
        assertEquals(5, sim.getMemory().getBlockInterval(4).getLowAddress());
        assertEquals(6, sim.getMemory().getBlockInterval(4).getHighAddress());
        sim.run(1);
        assertEquals(7, sim.getMemory().getBlockInterval(5).getLowAddress());
        assertEquals(7, sim.getMemory().getBlockInterval(5).getHighAddress());
        sim.run(1);
        assertEquals(2, sim.getMemory().getBlockInterval(6).getLowAddress());
        assertEquals(3, sim.getMemory().getBlockInterval(6).getHighAddress());
        mem.printMemory();
    }

    @Test
    void testTwoMemories() {
        MemoryImpl memEq = new MemoryImpl(10);
        MemoryImpl memEq1 = new MemoryImpl(10);
        MemoryImpl memDiff = new MemoryImpl(10);
        MemoryImpl memDiff1 = new MemoryImpl(20);

        memEq.allocate(StrategyType.FIRST_FIT, 1, 2);
        memEq.allocate(StrategyType.FIRST_FIT, 2, 2);
        memEq.allocate(StrategyType.FIRST_FIT, 3, 4);
        memEq.allocate(StrategyType.FIRST_FIT, 4, 1);
        memEq.deallocate(2);

        memEq1.allocate(StrategyType.FIRST_FIT, 1, 2);
        memEq1.allocate(StrategyType.FIRST_FIT, 2, 2);
        memEq1.allocate(StrategyType.FIRST_FIT, 3, 4);
        memEq1.allocate(StrategyType.FIRST_FIT, 4, 1);
        memEq1.deallocate(2);

        memDiff.allocate(StrategyType.FIRST_FIT, 2, 1);
        memDiff.allocate(StrategyType.FIRST_FIT, 3, 3);
        memDiff.allocate(StrategyType.FIRST_FIT, 4, 3);
        memDiff.allocate(StrategyType.FIRST_FIT, 5, 2);
        memDiff.deallocate(3);

        memDiff1.allocate(StrategyType.FIRST_FIT, 2, 1);
        memDiff1.allocate(StrategyType.FIRST_FIT, 3, 3);
        memDiff1.allocate(StrategyType.FIRST_FIT, 4, 3);
        memDiff1.allocate(StrategyType.FIRST_FIT, 5, 2);
        memDiff1.deallocate(3);

        assertEquals(memEq, memEq1);
        assertNotEquals(memEq, memDiff);
        assertNotEquals(memEq, memDiff1);
        assertNotEquals(memDiff, memDiff1);
    }

    @Test
    void testNeighbouringBlocks() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1,2),
                new AllocationInstruction(2,4),
                new AllocationInstruction(3,1),
                new AllocationInstruction(4,1),
                new DeallocationInstruction(2),
                new AllocationInstruction(5, 3),
                new AllocationInstruction(6, 1),
                new DeallocationInstruction(5)

        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.BEST_FIT);

        sim.run(1);
        assertTrue(sim.getMemory().neighboringBlocks(1).isEmpty());
        sim.run(1);
        assertTrue(sim.getMemory().neighboringBlocks(1).size() == 1);
        assertTrue(sim.getMemory().neighboringBlocks(2).size() == 1);
        sim.run(1);
        assertTrue(sim.getMemory().neighboringBlocks(2).size() == 2);
        //stopped here, cause fixed nullpointer exception
        mem.printMemory();
    }

    @Test
    void testFullMemory() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1, 1),
                new AllocationInstruction(2, 9),
                new AllocationInstruction(3, 2),
                new AllocationInstruction(4, 3),
                new AllocationInstruction(5, 2),
                new DeallocationInstruction(1),
                new DeallocationInstruction(3),
                new DeallocationInstruction(5),
                new AllocationInstruction(6, 3)
                //new AllocationInstruction(6, 18)

        ));

        MemoryImpl mem = new MemoryImpl(17);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.WORST_FIT);

        sim.run(5);
        assertTrue(mem.freeSlots().size() == 0);
        sim.runAll();
        mem.printMemory();
        assertTrue(sim.getExceptions().size() == 1);
    }

    @Test
    void testInstructions() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1, 1),
                new AllocationInstruction(2, 5),
                new AllocationInstruction(3, 2),
                new AllocationInstruction(4, 4),
                new AllocationInstruction(5, 1),
                new AllocationInstruction(6, 2),
                new AllocationInstruction(7, 2),
                new DeallocationInstruction(1),
                new DeallocationInstruction(5),
                //new DeallocationInstruction(7),
                new AllocationInstruction(8, 2)

        ));

        MemoryImpl mem = new MemoryImpl(17);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.runAll();
        mem.printMemory();
        assertTrue(sim.getExceptions().size() == 1);
    }

    @Test
    void randomTest() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(1, 1),
                new AllocationInstruction(2, 1),
                new AllocationInstruction(3, 1),
                new AllocationInstruction(4, 1),
                new AllocationInstruction(5, 1),
                new AllocationInstruction(6, 1),
                new AllocationInstruction(7, 1),
                new AllocationInstruction(8, 1),
                new AllocationInstruction(9, 1),
                new DeallocationInstruction(2),
                new DeallocationInstruction(4),
                new DeallocationInstruction(6),
                new DeallocationInstruction(8)
        ));

        MemoryImpl mem = new MemoryImpl(10);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.runAll();
        assertTrue(mem.neighboringBlocks(9).size() == 0);
        for (BlockInterval blockInterval : mem.freeSlots()) {
                System.out.println(blockInterval.getLowAddress() + 1);
                System.out.println(blockInterval.getHighAddress() + 1);
                System.out.println();
        }
    }

    @Test
    void test1() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(100, 1000),
                new AllocationInstruction(1, 500),
                new DeallocationInstruction(100),
                new AllocationInstruction(2, 200),
                new CompactInstruction(),
                new DeallocationInstruction(2),
                new AllocationInstruction(3, 500),
                new DeallocationInstruction(1),
                new AllocationInstruction(4, 100),
                new CompactInstruction()
        ));

        MemoryImpl mem = new MemoryImpl(2000);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        assertTrue(mem.blocks().size() == 0);

        assertTrue(mem.blockDimension(1) == 0);
        
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);

        assertTrue(mem.blocks().size() == 2);
        assertTrue(mem.blocks().get(0) == 4);
        assertTrue(mem.blocks().get(1) == 3);

        assertTrue(mem.containsBlock(3));
        assertTrue(mem.containsBlock(4));
        assertFalse(mem.containsBlock(101));
        assertFalse(mem.containsBlock(100));
        assertFalse(mem.containsBlock(-1));

         
    @Test
    void test2() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(100, 600),
                new AllocationInstruction(1, 200),
                new AllocationInstruction(2, 200),
                new DeallocationInstruction(1),
                new DeallocationInstruction(10),
                new AllocationInstruction(3, 100),
                new AllocationInstruction(4, 100),
                new CompactInstruction(),
                new DeallocationInstruction(3),
                new AllocationInstruction(5, 200),
                new DeallocationInstruction(5),
                new DeallocationInstruction(4),
                new AllocationInstruction(6, 200)
        ));

        MemoryImpl mem = new MemoryImpl(500);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.BEST_FIT);

        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        mem.printMemory();
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);    // 3
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);    // 4
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);

        mem.printMemory();
    }

    @Test
    void test3() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(100, 600),
                new AllocationInstruction(1, 200),
                new AllocationInstruction(2, 200),
                new DeallocationInstruction(1),
                new DeallocationInstruction(10),
                new AllocationInstruction(3, 100),
                new AllocationInstruction(4, 100),
                new CompactInstruction(),
                new DeallocationInstruction(3),
                new AllocationInstruction(5, 200),
                new DeallocationInstruction(5),
                new DeallocationInstruction(4),
                new AllocationInstruction(6, 200)
        ));

        MemoryImpl mem = new MemoryImpl(50);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.FIRST_FIT);

        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 4);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 5);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 6);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 7);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 7);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 8);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 9);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 10);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 11);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 12);

        assertTrue(mem.blocks().size() == 0);
        mem.printMemory();
    }

    @Test
    void test4() {
        Queue<Instruction> instr = new ArrayDeque<>(Arrays.asList(
                new AllocationInstruction(100, 60),
                new AllocationInstruction(1, 40),
                new AllocationInstruction(2, 30),
                new AllocationInstruction(3, 30),
                new AllocationInstruction(4, 60),
                new DeallocationInstruction(100),
                new DeallocationInstruction(3),

                new AllocationInstruction(5, 20),
                new AllocationInstruction(6, 40),
                new DeallocationInstruction(4),

                new AllocationInstruction(7, 10),
                new AllocationInstruction(8, 60),
                new AllocationInstruction(9, 40),
                new CompactInstruction(),

                new DeallocationInstruction(8),
                new AllocationInstruction(10, 30),
                new DeallocationInstruction(6),
                new DeallocationInstruction(9)

        ));

        MemoryImpl mem = new MemoryImpl(200);
        SimulationInstance sim = new SimulationInstanceImpl(
                instr,
                mem,
                StrategyType.BEST_FIT);
        
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 0);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        assertEquals(sim.getExceptions().get(0).getInstructionType(), AllocationInstruction.class);
        
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 1);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        assertEquals(sim.getExceptions().get(1).getInstructionType(), DeallocationInstruction.class);
        
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 2);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        assertEquals(sim.getExceptions().get(2).getInstructionType(), AllocationInstruction.class);
        mem.printMemory();
        
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 3);
        sim.run(1);
        assertTrue(sim.getExceptions().size() == 4);
        assertEquals(sim.getExceptions().get(3).getInstructionType(), DeallocationInstruction.class);

        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 0);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 0);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 0);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 0);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 1);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 1);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 1);
        //assertEquals(sim.getExceptions().get(0).getInstructionType(), AllocationInstruction.class);
//
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 1);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 1);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 2);
        //assertEquals(sim.getExceptions().get(1).getInstructionType(), DeallocationInstruction.class);
//
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 2);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 3);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 3);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 3);
        //assertEquals(sim.getExceptions().get(2).getInstructionType(), AllocationInstruction.class);
//
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 4);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 5);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 5);
        //sim.run(1);
        //assertTrue(sim.getExceptions().size() == 5);
        //assertEquals(sim.getExceptions().get(3).getInstructionType(), DeallocationInstruction.class);

        //mem.printMemory();
    }
}
