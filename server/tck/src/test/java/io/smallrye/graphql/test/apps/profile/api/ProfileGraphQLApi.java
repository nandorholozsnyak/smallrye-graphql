package io.smallrye.graphql.test.apps.profile.api;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

/**
 * Profile API
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@GraphQLApi
public class ProfileGraphQLApi {

    @Query("profile")
    @Description("Get a Profile by ID")
    public Profile getProfile(int profileId) {
        return ProfileDB.getProfile(profileId);
    }

    @Query("configurationByName")
    @Description("Get a configuration by name")
    public Configuration getByName(@Name("name") ConfigurationEnum name) {
        return Configuration.getByName(name.toString());
    }

}
