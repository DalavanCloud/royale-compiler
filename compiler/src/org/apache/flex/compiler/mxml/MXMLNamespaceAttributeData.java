/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.apache.flex.compiler.mxml;

import java.util.Collection;
import java.util.ListIterator;

import org.apache.flex.compiler.filespecs.IFileSpecification;
import org.apache.flex.compiler.internal.mxml.MXMLDialect;
import org.apache.flex.compiler.internal.parsing.mxml.MXMLToken;
import org.apache.flex.compiler.problems.ICompilerProblem;

/**
 * Represents an xmlns style namespace import found within the MXML language
 */
public class MXMLNamespaceAttributeData extends MXMLTagAttributeData implements
        IMXMLNamespaceAttributeData
{
    /**
     * Constructor.
     */
    MXMLNamespaceAttributeData(MXMLToken nameToken, ListIterator<MXMLToken> tokenIterator, MXMLDialect mxmlDialect, IFileSpecification spec, Collection<ICompilerProblem> problems)
    {
        super(nameToken, tokenIterator, mxmlDialect, spec, problems);

        if (attributeName.startsWith("xmlns:"))
            prefix = attributeName.substring(6);
    }

    private String prefix = "";

    //
    // IMXMLNamepsaceAttributeData implementations
    //

    @Override
    public String getNamespacePrefix()
    {
        return prefix;
    }

    @Override
    public String getNamespace()
    {
        return getRawValue();
    }
}
