package jpolanco.springbootapp.shared.infrastructure;

import lombok.NonNull;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @Override
    @NonNull
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private record StringToEnumConverter<T extends Enum<?>>(Class<T> enumType) implements Converter<String, T> {

        @Override
            public T convert(String source) {
                for (T constant : enumType.getEnumConstants()) {
                    if (constant.name().equalsIgnoreCase(source)) {
                        return constant;
                    }
                }
                throw new IllegalArgumentException("No enum constant " + enumType.getSimpleName() + " for value: " + source);
            }
        }
}

