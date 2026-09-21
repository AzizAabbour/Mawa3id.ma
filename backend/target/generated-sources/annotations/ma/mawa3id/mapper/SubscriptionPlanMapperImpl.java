package ma.mawa3id.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.mawa3id.domain.entity.SubscriptionPlan;
import ma.mawa3id.dto.subscription.SubscriptionPlanResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-21T16:14:26+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class SubscriptionPlanMapperImpl implements SubscriptionPlanMapper {

    @Override
    public SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
        if ( plan == null ) {
            return null;
        }

        SubscriptionPlanResponse.SubscriptionPlanResponseBuilder subscriptionPlanResponse = SubscriptionPlanResponse.builder();

        subscriptionPlanResponse.active( plan.isActive() );
        subscriptionPlanResponse.description( plan.getDescription() );
        subscriptionPlanResponse.displayOrder( plan.getDisplayOrder() );
        List<String> list = plan.getFeatures();
        if ( list != null ) {
            subscriptionPlanResponse.features( new ArrayList<String>( list ) );
        }
        subscriptionPlanResponse.id( plan.getId() );
        subscriptionPlanResponse.maxAppointmentsPerMonth( plan.getMaxAppointmentsPerMonth() );
        subscriptionPlanResponse.maxCustomers( plan.getMaxCustomers() );
        subscriptionPlanResponse.maxEmployees( plan.getMaxEmployees() );
        subscriptionPlanResponse.name( plan.getName() );
        subscriptionPlanResponse.priceMonthlyMad( plan.getPriceMonthlyMad() );
        subscriptionPlanResponse.publicId( plan.getPublicId() );
        subscriptionPlanResponse.slug( plan.getSlug() );
        subscriptionPlanResponse.smsNotificationsIncluded( plan.isSmsNotificationsIncluded() );
        subscriptionPlanResponse.whatsappNotificationsIncluded( plan.isWhatsappNotificationsIncluded() );

        return subscriptionPlanResponse.build();
    }

    @Override
    public List<SubscriptionPlanResponse> toResponseList(List<SubscriptionPlan> plans) {
        if ( plans == null ) {
            return null;
        }

        List<SubscriptionPlanResponse> list = new ArrayList<SubscriptionPlanResponse>( plans.size() );
        for ( SubscriptionPlan subscriptionPlan : plans ) {
            list.add( toResponse( subscriptionPlan ) );
        }

        return list;
    }
}
