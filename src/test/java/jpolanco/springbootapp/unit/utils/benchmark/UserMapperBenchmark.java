package jpolanco.springbootapp.unit.utils.benchmark;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.UserEntityMapperImpl;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.RoleEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class UserMapperBenchmark {
    private static UserEntity buildSampleUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setFirstName("Antonio");
        entity.setLastName("Test");
        entity.setEmail("antonio@gmail.com");
        entity.setPassword("hashedpass");
        entity.setRoles(Set.of(new RoleEntity("ADMIN")));
        entity.setStatus(UserStatus.ACTIVE);
        entity.setQrFileName("file");
        entity.setCreatedAt(Instant.now());
        return entity;
    }

    private static User buildSampleUser() {
        return User.create(
                "Antonio",
                "Test",
                "example@gmail.com",
                "password123"
        ).getSuccess();
    }
    public static class ToDomain {
        public static void main(String[] args) {

            final int warmupRounds = 5_000;
            final int testRounds = 10_000;

            var entity = buildSampleUserEntity();
            var mapper = new UserEntityMapperImpl();

            System.out.println("Warming up with " + warmupRounds + " rounds...");

            // 🔥 Warmup (ignored)
            for (int i = 0; i < warmupRounds; i++) {
                mapper.toDomain(entity);
            }

            System.out.println("Starting timed benchmark...");

            // ⏱ Real measurement
            long totalTime = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (int i = 0; i < testRounds; i++) {
                long start = System.nanoTime();
                mapper.toDomain(entity);
                long duration = System.nanoTime() - start;

                totalTime += duration;
                if (duration < min) min = duration;
                if (duration > max) max = duration;
            }

            long average = totalTime / testRounds;

            System.out.println("Benchmark results (toDomain without validation)");
            System.out.println("--------------------------------------------");
            System.out.println("Warmup rounds: " + warmupRounds);
            System.out.println("Test rounds:   " + testRounds);
            System.out.println("Average time:  " + average + " ns");
            System.out.println("Min time:      " + min + " ns");
            System.out.println("Max time:      " + max + " ns");
        }
    }

    public static class ToEntity {
        public static void main(String[] args) {
            final int warmupRounds = 5_000;
            final int testRounds = 10_000;

            var user = buildSampleUser();
            var mapper = new UserEntityMapperImpl();

            System.out.println("Warming up with " + warmupRounds + " rounds...");

            // 🔥 Warmup (ignored)
            for (int i = 0; i < warmupRounds; i++) {
                mapper.toEntity(user);
            }

            System.out.println("Starting timed benchmark...");

            // ⏱ Real measurement
            long totalTime = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (int i = 0; i < testRounds; i++) {
                long start = System.nanoTime();
                mapper.toEntity(user);
                long duration = System.nanoTime() - start;

                totalTime += duration;
                if (duration < min) min = duration;
                if (duration > max) max = duration;
            }

            long average = totalTime / testRounds;

            System.out.println("Benchmark results (fromDomain)");
            System.out.println("-----------------------------");
            System.out.println("Warmup rounds: " + warmupRounds);
            System.out.println("Test rounds:   " + testRounds);
            System.out.println("Average time:  " + average + " ns");
            System.out.println("Min time:      " + min + " ns");
            System.out.println("Max time:      " + max + " ns");
        }
    }
}
