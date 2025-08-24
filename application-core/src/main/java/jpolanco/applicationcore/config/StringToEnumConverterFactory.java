package jpolanco.applicationcore.config;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * A factory for converting String values to Enum types in a case-insensitive manner.
 * This converter factory can be used in Spring applications to automatically convert
 * String parameters (e.g., from HTTP requests) to their corresponding Enum types.
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @Override
    @NonNull
    public <T extends Enum<?>> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private record StringToEnumConverter<T extends Enum<?>>(Class<T> enumType) implements Converter<String, T> {

        @Override
        public T convert(@NonNull String source) {
            for (T constant : enumType.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(source)) {
                    return constant;
                }
            }
            throw new IllegalArgumentException("No enum constant " + enumType.getSimpleName() + " for value: " + source);
        }
    }
}

