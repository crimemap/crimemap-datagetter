/* Copyright (C) 2014 Miroslav Bimbo
*
* This file is part of Crimemap.
*
* Crimemap is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Crimemap is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with Crimemap. If not, see <http://www.gnu.org/licenses/>.
*/
package sk.mapazlocinu.datagetter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * An implementation of <a
 * href="http://java.sun.com/javase/6/docs/api/javax/xml/namespace/NamespaceContext.html">
 * NamespaceContext </a>. Instances are immutable.
 * 
 * @author McDowell
 */
public final class NamespaceContextMap implements
    NamespaceContext {

  private final Map<String, String> prefixMap;
  private final Map<String, Set<String>> nsMap;

  /**
   * Constructor that takes a map of XML prefix-namespaceURI values. A defensive
   * copy is made of the map. An IllegalArgumentException will be thrown if the
   * map attempts to remap the standard prefixes defined in the NamespaceContext
   * contract.
   * 
   * @param prefixMappings
   *          a map of prefix:namespaceURI values
   */
  public NamespaceContextMap(
      Map<String, String> prefixMappings) {
    prefixMap = createPrefixMap(prefixMappings);
    nsMap = createNamespaceMap(prefixMap);
  }

  /**
   * Convenience constructor.
   * 
   * @param mappingPairs
   *          pairs of prefix-namespaceURI values
   */
  public NamespaceContextMap(String... mappingPairs) {
    this(toMap(mappingPairs));
  }

  private static Map<String, String> toMap(
      String... mappingPairs) {
    Map<String, String> prefixMappings = new HashMap<String, String>(
        mappingPairs.length / 2);
    for (int i = 0; i < mappingPairs.length; i++) {
      prefixMappings
          .put(mappingPairs[i], mappingPairs[++i]);
    }
    return prefixMappings;
  }

  private Map<String, String> createPrefixMap(
      Map<String, String> prefixMappings) {
    Map<String, String> prefixMap = new HashMap<String, String>(
        prefixMappings);
    addConstant(prefixMap, XMLConstants.XML_NS_PREFIX,
        XMLConstants.XML_NS_URI);
    addConstant(prefixMap, XMLConstants.XMLNS_ATTRIBUTE,
        XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    return Collections.unmodifiableMap(prefixMap);
  }

  private void addConstant(Map<String, String> prefixMap,
      String prefix, String nsURI) {
    String previous = prefixMap.put(prefix, nsURI);
    if (previous != null && !previous.equals(nsURI)) {
      throw new IllegalArgumentException(prefix + " -> "
          + previous + "; see NamespaceContext contract");
    }
  }

  private Map<String, Set<String>> createNamespaceMap(
      Map<String, String> prefixMap) {
    Map<String, Set<String>> nsMap = new HashMap<String, Set<String>>();
    for (Map.Entry<String, String> entry : prefixMap
        .entrySet()) {
      String nsURI = entry.getValue();
      Set<String> prefixes = nsMap.get(nsURI);
      if (prefixes == null) {
        prefixes = new HashSet<String>();
        nsMap.put(nsURI, prefixes);
      }
      prefixes.add(entry.getKey());
    }
    for (Map.Entry<String, Set<String>> entry : nsMap
        .entrySet()) {
      Set<String> readOnly = Collections
          .unmodifiableSet(entry.getValue());
      entry.setValue(readOnly);
    }
    return nsMap;
  }

  public String getNamespaceURI(String prefix) {
    checkNotNull(prefix);
    String nsURI = prefixMap.get(prefix);
    return nsURI == null ? XMLConstants.NULL_NS_URI : nsURI;
  }

  public String getPrefix(String namespaceURI) {
    checkNotNull(namespaceURI);
    Set<String> set = nsMap.get(namespaceURI);
    return set == null ? null : set.iterator().next();
  }

  public Iterator<String> getPrefixes(String namespaceURI) {
    checkNotNull(namespaceURI);
    Set<String> set = nsMap.get(namespaceURI);
    return set.iterator();
  }

  private void checkNotNull(String value) {
    if (value == null) {
      throw new IllegalArgumentException("null");
    }
  }

  /**
   * @return an unmodifiable map of the mappings in the form prefix-namespaceURI
   */
  public Map<String, String> getMap() {
    return prefixMap;
  }

}