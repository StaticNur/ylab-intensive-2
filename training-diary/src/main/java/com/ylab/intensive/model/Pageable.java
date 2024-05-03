package com.ylab.intensive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents pagination information for retrieving a page of data.
 * <p>
 * This class encapsulates pagination information, including the page number and the number of items per page.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {
    /**
     * The page number.
     */
    private int page;

    /**
     * The number of items per page.
     */
    private int count;
}

