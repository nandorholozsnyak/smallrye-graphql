/*
 * Copyright 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.smallrye.graphql.schema.type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.jandex.Type;

import graphql.schema.GraphQLEnumType;
import io.smallrye.graphql.schema.Annotations;
import io.smallrye.graphql.schema.helper.AnnotationsHelper;
import io.smallrye.graphql.schema.helper.DescriptionHelper;
import io.smallrye.graphql.schema.helper.NameHelper;

/**
 * Create a map of all Enums.
 * 
 * It produces a maps, that can be injected anywhere in the code:
 * - enumMap - contains all enum types.
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
public class EnumTypeCreator implements Creator {

    @Inject
    private Map<DotName, GraphQLEnumType> enumMap;

    @Inject
    private NameHelper nameHelper;

    @Inject
    private DescriptionHelper descriptionHelper;

    @Inject
    private AnnotationsHelper annotationsHelper;

    @Override
    public GraphQLEnumType create(ClassInfo classInfo) {
        if (enumMap.containsKey(classInfo.name())) {
            return enumMap.get(classInfo.name());
        } else {
            Annotations annotations = annotationsHelper.getAnnotationsForClass(classInfo);
            String name = nameHelper.getEnumName(classInfo, annotations);

            GraphQLEnumType.Builder builder = GraphQLEnumType.newEnum()
                    .name(name);

            // Description
            Optional<String> maybeDescription = descriptionHelper.getDescription(annotations);
            builder = builder.description(maybeDescription.orElse(null));

            // Values
            List<FieldInfo> fields = classInfo.fields();
            for (FieldInfo field : fields) {
                if (!field.type().kind().equals(Type.Kind.ARRAY)) {
                    builder = builder.value(field.name());
                }
            }
            GraphQLEnumType enumType = builder.build();
            enumMap.put(classInfo.name(), enumType);
            return enumType;
        }
    }

}