/*
 * $#
 * FOS API
 *  
 * Copyright (C) 2013 Feedzai SA
 *  
 * This software is licensed under the Apache License, Version 2.0 (the "Apache License") or the GNU
 * Lesser General Public License version 3 (the "GPL License"). You may choose either license to govern
 * your use of this software only upon the condition that you accept all of the terms of either the Apache
 * License or the LGPL License.
 * 
 * You may obtain a copy of the Apache License and the LGPL License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Apache License
 * or the LGPL License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Apache License and the LGPL License for the specific language governing
 * permissions and limitations under the Apache License and the LGPL License.
 * #$
 */
package com.feedzai.fos.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.feedzai.fos.common.validation.NotBlank;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;


/**
 * A CategoricalAttribute will be mapped into the underlying implementation categorical type.
 * It holds a list of all possible categorical values and the ability to replace a unknown categorical value
 * by a default value.
 */
public final class CategoricalAttribute extends Attribute {
    @JsonIgnore
    public final static String DEFAULT_UNKNOWN_REPLACEMENT = "__UNKOWN__";

    private List<String> categoricalInstances;


    private String unknownReplacement = DEFAULT_UNKNOWN_REPLACEMENT;

    @JsonIgnore
    private int unknownReplacementIndex;

    @JsonIgnore
    private boolean isClass = false;


    /**
     * Creates a new categorical with the given <code>name</code>, <code>type</code> and <code>categoricalInstances</code>.
     * <p/> The <code>categoricalInstances</code> is the set of possible values that the field can take.
     *
     * @param name             the name of the field
     * @param pcategoricalInstances for <code>type=nominal</code>, defines the possibles values of the field
     * @param punknownReplacement replacement value for unknown categoricals
     */
    @JsonCreator
    public CategoricalAttribute(@NotBlank @JsonProperty("name") String name,
                                @JsonProperty("categoricalInstances") List<String> pcategoricalInstances,
                                @JsonProperty("unknownReplacement") String punknownReplacement) {
        super(name);
        checkArgument(pcategoricalInstances != null && pcategoricalInstances.size() > 0, "Missing instances for nominal value");
        checkArgument(!pcategoricalInstances.contains(""), "Nominal instances values must not empty");
        checkArgument(!pcategoricalInstances.contains(null), "Nominal instances must not be null");


        categoricalInstances = new ArrayList<>(pcategoricalInstances);

        unknownReplacement = punknownReplacement == null ? DEFAULT_UNKNOWN_REPLACEMENT : punknownReplacement;

        // add unknown replacement if it isn't  already part of the list
        if (!categoricalInstances.contains(unknownReplacement)) {
            categoricalInstances.add(unknownReplacement);
        }

        Collections.sort(categoricalInstances);

        unknownReplacementIndex = Collections.binarySearch(categoricalInstances, unknownReplacement);
    }

    /**
     * Creates a new categorical with the given <code>name</code>, <code>type</code> and <code>categoricalInstances</code>.
     * <p/> The <code>categoricalInstances</code> is the set of possible values that the field can take (only valid for nominal).
     *
     * @param name             the name of the field
     * @param categoricalInstances for <code>type=nominal</code>, defines the possibles values of the field
     */
    public CategoricalAttribute(@NotBlank String name, List<String> categoricalInstances) {
        this(name, categoricalInstances, null);
    }



    /**
     * Gets the list of nominal values for this field (if the <code>type=Nominal</code>) or null otherwise.
     *
     * @return a list of nominal values (can be null!!)
     */
    public List<String> getCategoricalInstances() {
        return ImmutableList.copyOf(categoricalInstances);
    }

    public String getUnknownReplacement() {
        return unknownReplacement;
    }

    /**
     * Marks this Categorical as a classifier
     */
    public void setClass() {
        if (isClass) {
            return;
        }
        this.isClass = true;


        ArrayList<String> newCategorical = new ArrayList<>(categoricalInstances.size()-1);
        for(String value : categoricalInstances) {
            if (!value.equals(unknownReplacement)) {
                newCategorical.add(value);
            }
        }
        categoricalInstances = newCategorical;
    }


    @Override
    public double parse(Object original, InstanceType type) throws FOSException {
        if (original == null) {
            return Double.NaN;
        }
        String value = original.toString();

        int index = Collections.binarySearch(categoricalInstances, value);
        if ((isClass && type == InstanceType.TRAINING) && index < 0) {
            throw new FOSException("Invalid value "
                                   + original
                                   + " for classifier, valid values are "
                                   + Joiner.on(',').join(categoricalInstances));

        }

        return index < 0 ? unknownReplacementIndex : index;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(categoricalInstances);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final CategoricalAttribute other = (CategoricalAttribute) obj;
        return Objects.equal(categoricalInstances, other.categoricalInstances);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(getName())
                .add("categoricalInstances", categoricalInstances)
                .add("unknownReplacement", unknownReplacement).toString();

    }

    public int getUnknownReplacementIndex() {
        return unknownReplacementIndex;
    }
}
