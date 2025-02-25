package pl.excellentapp.brewery.beer.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorProviderTest {

    private final IdGeneratorProvider idGeneratorProvider = new IdGeneratorProvider();

    @Test
    void shouldGenerateNonNullUUID() {
        // when
        final var generatedUUID = idGeneratorProvider.random();

        // then
        assertThat(generatedUUID).isNotNull();
    }

    @Test
    void shouldGenerateUniqueUUIDs() {
        // when
        final var firstUUID = idGeneratorProvider.random();
        final var secondUUID = idGeneratorProvider.random();

        // then
        assertThat(firstUUID).isNotNull();
        assertThat(secondUUID).isNotNull();
        assertThat(firstUUID).isNotEqualTo(secondUUID);
    }
}