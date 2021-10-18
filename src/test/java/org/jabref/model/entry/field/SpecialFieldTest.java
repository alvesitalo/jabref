package org.jabref.model.entry.field;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpecialFieldTest {

    @Test
    public void getSpecialFieldInstanceFromFieldNameValid() {
        assertEquals(Optional.of(SpecialField.RANKING),
                SpecialField.fromName("ranking"));
    }

    @Test
    public void getSpecialFieldInstanceFromFieldNameEmptyForInvalidField() {
        assertEquals(Optional.empty(), SpecialField.fromName("title"));
    }

    @Test
    public void getRating1FromStaticSpecialFieldValue() {
        assertEquals(SpecialFieldValue.getRating(1),
                SpecialFieldValue.RANK_1);
    }

    @Test
    public void getRating2FromStaticSpecialFieldValue() {
        assertEquals(SpecialFieldValue.getRating(2),
                SpecialFieldValue.RANK_2);
    }

    @Test
    public void getRating3FromStaticSpecialFieldValue() {
        assertEquals(SpecialFieldValue.getRating(3),
                SpecialFieldValue.RANK_3);
    }

    @Test
    public void getRating4FromStaticSpecialFieldValue() {
        assertEquals(SpecialFieldValue.getRating(4),
                SpecialFieldValue.RANK_4);
    }

    @Test
    public void getRating5FromStaticSpecialFieldValue() {
        assertEquals(SpecialFieldValue.getRating(5),
                SpecialFieldValue.RANK_5);
    }

    @Test
    public void getRating6OrMoreFromStaticSpecialFieldValue() {
        UnsupportedOperationException thrown = assertThrows(
                UnsupportedOperationException.class,
                () -> SpecialFieldValue.getRating(6),
                "6is not a valid ranking"
        );

        assertTrue(thrown.getMessage().contains("6is not a valid ranking"));
    }

}
