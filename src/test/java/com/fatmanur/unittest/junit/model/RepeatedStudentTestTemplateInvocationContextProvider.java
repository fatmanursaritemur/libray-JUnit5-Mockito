package com.fatmanur.unittest.junit.model;

import com.fatmanur.unittest.model.AttendantLibraryRecord;
import com.fatmanur.unittest.model.Library;
import com.fatmanur.unittest.model.Semester;
import org.junit.jupiter.api.extension.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class RepeatedStudentTestTemplateInvocationContextProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getRequiredTestClass() == MemberTestWithTemplate.class;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(
                testTemplateInvocationContext("101", 1),
                testTemplateInvocationContext("103", 2)


        );
    }

    private TestTemplateInvocationContext testTemplateInvocationContext(String libraryCode, int numberOfCall) {

        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return "Add Course to Student ==> Add one book to member and member has " + numberOfCall + " books";
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new ParameterResolver() {
                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

                        if (parameterContext.getIndex() == 0 && parameterContext.getParameter().getType() == AttendantLibraryRecord.class) {
                            return true;
                        }

                        return parameterContext.getIndex() == 1 && parameterContext.getParameter().getType() == int.class;
                    }

                    @Override
                    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

                        if (parameterContext.getParameter().getType() == AttendantLibraryRecord.class) {
                            return new AttendantLibraryRecord(new Library(libraryCode), new Semester());
                        }

                        if (parameterContext.getParameter().getType() == int.class) {
                            return numberOfCall;
                        }

                        throw new IllegalArgumentException("not supporter parameter");
                    }
                });
            }
        };
    }
}