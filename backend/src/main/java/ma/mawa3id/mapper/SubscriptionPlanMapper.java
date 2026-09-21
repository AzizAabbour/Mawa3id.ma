package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.SubscriptionPlan;
import ma.mawa3id.dto.subscription.SubscriptionPlanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionPlanMapper {
    SubscriptionPlanResponse toResponse(SubscriptionPlan plan);
    List<SubscriptionPlanResponse> toResponseList(List<SubscriptionPlan> plans);
}
