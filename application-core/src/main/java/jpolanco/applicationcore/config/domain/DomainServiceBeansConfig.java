package jpolanco.applicationcore.config.domain;

import jpolanco.applicationcore.user.domain.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceBeansConfig {

    @Bean("passwordStrongest")
    public PasswordStrongestPolicyService passwordStrongestPolicyService() {
        return new PasswordStrongestPolicyService();
    }

    @Bean("reactivationUser")
    public ReactivationPolicyService reactivationPoliciesService() {
        return new ReactivationPolicyService();
    }

    @Bean("deactivationUser")
    public DeactivationPolicyService deactivationPoliciesService() {
        return new DeactivationPolicyService();
    }

    @Bean("deletionUser")
    public DeletionPolicyService deletionPoliciesService() {
        return new DeletionPolicyService();
    }

    @Bean("suspensionUser")
    public SuspensionPolicyService suspensionPoliciesService() {
        return new SuspensionPolicyService();
    }

    @Bean("userUpdate")
    public UserUpdatePolicyService userUpdatePoliciesService(
            ReactivationPolicyService reactivationPoliciesService,
            DeactivationPolicyService deactivationPoliciesService,
            SuspensionPolicyService suspensionPoliciesService
    ) {
        return new UserUpdatePolicyService(
                deactivationPoliciesService,
                reactivationPoliciesService,
                suspensionPoliciesService
        );
    }
}
