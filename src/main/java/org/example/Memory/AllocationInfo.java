package org.example.Memory;

import lombok.Getter;

/**
 * Record representing information about an active memory allocation.
 *
 * @param id       the unique identifier of the allocation
 * @param start    the starting index of the allocation in memory
 * @param size     the total size of the allocation in bytes
 * @param usedSize the actual used size of the allocation in bytes (same as size for contiguous allocations)
 */
public record AllocationInfo(int id, int start, int size, int usedSize) {
}

