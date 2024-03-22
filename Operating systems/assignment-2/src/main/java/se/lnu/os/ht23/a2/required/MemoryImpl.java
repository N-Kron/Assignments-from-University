package se.lnu.os.ht23.a2.required;

import se.lnu.os.ht23.a2.provided.data.BlockInterval;
import se.lnu.os.ht23.a2.provided.data.StrategyType;
import se.lnu.os.ht23.a2.provided.interfaces.Memory;

import java.util.*;
import java.util.Map.Entry;


public class MemoryImpl implements Memory {

    private final int size;
    private BlockInterval[] mem;
    private Dictionary<Integer, Integer> dict;

    public MemoryImpl(int size){
        this.size = size;
        mem = new BlockInterval[size];
        dict = new Hashtable<>();

    }

    @Override
    public boolean containsBlock(int blockId) {
        return dict.get(blockId) != null;
    }

    @Override
    public List<Integer> blocks() {
        List<Integer> list = new ArrayList<>();
        for (Entry<Integer, Integer> entry : ((Hashtable<Integer, Integer>) dict).entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    @Override
    public int blockDimension(int blockId) {
        BlockInterval b = getBlockInterval(blockId);
        if (b == null) {
            return 0;
        } else {
            return b.getHighAddress() - b.getLowAddress() + 1;
        }
    }

    @Override
    public BlockInterval getBlockInterval(int blockId) {
        if (dict.get(blockId) == null) {
            return null;
        }
        return mem[dict.get(blockId)];
    }

    @Override
    public Set<Integer> neighboringBlocks(int blockId) {
        // if no neighbours, return empty set
        Set<Integer> neighbours = new HashSet<>();
        BlockInterval b = getBlockInterval(blockId);
        if (b == null) {
            return neighbours;
        }
        if (b.getLowAddress() != 0) {
            BlockInterval previous = mem[b.getLowAddress() - 1];
            if (previous != null) {
                neighbours.add(getBlockId(previous));
            }
        }
        if (b.getHighAddress() != size) {
            BlockInterval next = mem[b.getHighAddress() + 1];
            if (next != null) {
                neighbours.add(getBlockId(next));
            }

        }
        return neighbours;
    }

    @Override
    public double fragmentation() {
        // no need to round decimals
        int emptyCounter = 0;
        int biggestGap = 0;
        double totalEmpty = 0;

        for (BlockInterval b : mem) {
            if (b == null) {
                emptyCounter ++;
                totalEmpty ++;
            } else {
                if (emptyCounter > biggestGap) {
                    biggestGap = emptyCounter;
                }
                emptyCounter = 0;
            }
        }
        if (emptyCounter > biggestGap) {
            biggestGap = emptyCounter;
        }
        return 1 - (biggestGap/totalEmpty);
    }

    @Override
    public Set<BlockInterval> freeSlots() {
        Set<BlockInterval> freeSlots = new LinkedHashSet<>();
        int lowAddress = 0;
        boolean previous = true;
        int i;

        for (i = 0; i < mem.length; i++) {
            if (previous && mem[i] == null) {
                lowAddress = i;
                previous = false;
            } else if (mem[i] != null) {
                if (!previous) {
                    freeSlots.add(new BlockInterval(lowAddress, i - 1));
                }
                previous = true;
            }
        }
        if (!previous) {
            freeSlots.add(new BlockInterval(lowAddress, i - 1));
        }
        return freeSlots;
    }

    @Override
    public boolean equals(Object o) {
        /* TODO     -----> FOR EXCELLENT ONLY <-----
            Replace this return statement with the method that confronts two Memory objects. It is used by the tests
            whenever AssertEquals is called and should return true only when the Memories are structured exactly in
            the same way (same dimension, blocks and disposition), regardless of the Simulation they come from.
         */
        Memory toCheck = (Memory) o;
        BlockInterval[] local = this.freeSlots().toArray(BlockInterval[]::new);
        BlockInterval[] foreign = toCheck.freeSlots().toArray(BlockInterval[]::new);
        for (int i = 0; i < local.length; i++) {
            if (!(local[i].getLowAddress() == foreign[i].getLowAddress()
                && local[i].getHighAddress() == foreign[i].getHighAddress())) {
                    return false;
            }
        }

        List<Integer> localIds = this.blocks();
        List<Integer> foreignIds = toCheck.blocks();
        try {
            for (int i = 0; i < localIds.size(); i++) {
                if (!(Objects.equals(localIds.get(i), foreignIds.get(i)))) {
                    return false;
                } else if (!(this.getBlockInterval(localIds.get(i)).equals(toCheck.getBlockInterval(foreignIds.get(i))))) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean allocate(StrategyType s, int id, int dimension) {
        Set<BlockInterval> freeSlots = this.freeSlots();
        if (freeSlots.isEmpty() || dict.get(id) != null || id <= 0) {
            return false;
        }

        switch (s) {
            case FIRST_FIT:
                for (BlockInterval b : freeSlots) {
                    if ((b.getHighAddress() - b.getLowAddress()) + 1 >= dimension) {
                        if (allocateBlock(dimension, b.getLowAddress())) {
                            dict.put(id, b.getLowAddress());
                            return true;
                        }
                    }
                }
                break;

            case BEST_FIT:
                BlockInterval bestFit = null;
                BlockInterval temp = null;
                while (freeSlots.iterator().hasNext()) {
                    temp = freeSlots.iterator().next();
                    if (temp.getHighAddress() - temp.getLowAddress() + 1 - dimension >= 0) {
                        bestFit = temp;
                        break;
                    }
                    freeSlots.remove(temp);
                }
                if (bestFit == null) {
                    return false;
                }
                int compare = bestFit.getHighAddress() - bestFit.getLowAddress() + 1 - dimension;
                int tempInt = 0;

                for (BlockInterval b : freeSlots) {
                    tempInt = b.getHighAddress() - b.getLowAddress() + 1;
                    if (tempInt - dimension >= 0 && tempInt - dimension < compare) {
                        bestFit = b;
                        compare = bestFit.getHighAddress() - bestFit.getLowAddress() + 1 - dimension;
                    }
                }
                if (this.allocateBlock(dimension, bestFit.getLowAddress())) {
                    dict.put(id, bestFit.getLowAddress());
                    return true;
                }
                break;

            case WORST_FIT:
                BlockInterval biggest = freeSlots.iterator().next();

                for (BlockInterval b : freeSlots) {
                    if (b.getHighAddress() - b.getLowAddress() > biggest.getHighAddress() - biggest.getLowAddress()) {
                        biggest = b;
                    }
                }
                if ((biggest.getHighAddress() - biggest.getLowAddress()) + 1 >= dimension) {
                    if (allocateBlock(dimension, biggest.getLowAddress())) {
                        dict.put(id, biggest.getLowAddress());
                        return true;
                    }
                }
                break;
        
            default:
                break;
        }
        return false;
    }

    public boolean deallocate(int blockId) {
        BlockInterval b = this.getBlockInterval(blockId);
        if (b == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(mem[i] == null)) {
                if (mem[i].equals(b)) {
                    mem[i] = null;
                }
            }
        }
        dict.remove(blockId);
        return true;
    }

    private boolean allocateBlock(int dimension, int start) {
        if (dimension > 0) {
            BlockInterval b = new BlockInterval(start, start + (dimension - 1));
            for (int i = start; i <= b.getHighAddress(); i++) {
                mem[i] = b;
            }
            return true;
        }
        return false;
    }

    public void compact() {
        BlockInterval currentBlock = null;

        for (int i = 0; i < size; i++) {
            if (mem[i] != null && mem[i] != currentBlock) {
                currentBlock = mem[i];
                this.move(currentBlock);
            }
        }
    }

    private void move(BlockInterval b) {
        int id = this.getBlockId(b);
        int dim = this.blockDimension(id);
        this.deallocate(id);
        this.allocate(StrategyType.FIRST_FIT, id, dim);
    }

    // inspiration from https://www.baeldung.com/java-map-key-from-value
    private int getBlockId(BlockInterval b) {
        for (Entry<Integer, Integer> entry : ((Hashtable<Integer, Integer>) dict).entrySet()) {
            if (b.getLowAddress() == entry.getValue()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    // for testing purposes
    public void printMemory() {
        for (int i = 0; i < size; i++) {
            System.out.println(i + ": " + (mem[i] == null ? "null" : getBlockId(mem[i])));
        }
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder("Memory Size = " + size + "\n");
        if(blocks() != null) {
            for (int blockId : blocks()) {
                BlockInterval inter = getBlockInterval(blockId);
                retStr.append("(").append(inter.getLowAddress()).append("-").append(inter.getHighAddress()).append(")")
                        .append(" --> ").append("ID ").append(blockId).append("\n");
            }
        }
        if(freeSlots() != null) {
            for (BlockInterval bi : freeSlots()) {
                retStr.append("(").append(bi.getLowAddress()).append("-").append(bi.getHighAddress()).append(")")
                        .append(" --> ").append("EMPTY").append("\n");
            }
        }
        return retStr.toString();
    }
}
