package com.cooksys.ftd.assignments.day.three.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cooksys.ftd.assignments.day.three.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.day.three.collections.model.Capitalist;
import com.cooksys.ftd.assignments.day.three.collections.model.FatCat;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

	Map<FatCat, Set<Capitalist>> hierarchyCollection = new HashMap();

	/**
	 * Adds a given element to the hierarchy.
	 * <p>
	 * If the given element is already present in the hierarchy, do not add it
	 * and return false
	 * <p>
	 * If the given element has a parent and the parent is not part of the
	 * hierarchy, add the parent and then add the given element
	 * <p>
	 * If the given element has no parent but is a Parent itself, add it to the
	 * hierarchy
	 * <p>
	 * If the given element has no parent and is not a Parent itself, do not add
	 * it and return false
	 *
	 * @param capitalist
	 *            the element to add to the hierarchy
	 * @return true if the element was added successfully, false otherwise
	 */
	@Override
	public boolean add(Capitalist capitalist) {
		if (capitalist == null) {
			return false;
		}

		if (has(capitalist)) {
			return false;
		}

		if (capitalist instanceof FatCat) {
			HashSet<Capitalist> children = new HashSet<Capitalist>();
			hierarchyCollection.put((FatCat) capitalist, children);
		} else {
			if(!capitalist.hasParent())
				return false;
		}
		
		if (capitalist.hasParent()) {
			Capitalist parent = capitalist.getParent();
			if (has(parent)) {
				Set<Capitalist> parentsChildSet = getChildrenEditable((FatCat) parent);
				parentsChildSet.add(capitalist);
			} else {
				add(parent);
				Set<Capitalist> children = new HashSet<Capitalist>();
				children.add(capitalist);
				hierarchyCollection.put((FatCat) parent, children);
			}
			return true;
		}
		
		return true;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hierarchyCollection == null) ? 0 : hierarchyCollection.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MegaCorp other = (MegaCorp) obj;
		if (hierarchyCollection == null) {
			if (other.hierarchyCollection != null)
				return false;
		} else if (!hierarchyCollection.equals(other.hierarchyCollection))
			return false;
		return true;
	}

	/**
	 * @param capitalist
	 *            the element to search for
	 * @return true if the element has been added to the hierarchy, false
	 *         otherwise
	 */
	@Override
	public boolean has(Capitalist capitalist) {
		if (capitalist instanceof FatCat) {
			if(hierarchyCollection.containsKey(capitalist)){
				return true;
			}
		} else {
			for (FatCat fatCats : hierarchyCollection.keySet()) {
				if(hierarchyCollection.get(fatCats).contains(capitalist)) {
					return true;
				} 
			}
		}
		return false;
	}

	/**
	 * @return all elements in the hierarchy, or an empty set if no elements
	 *         have been added to the hierarchy
	 */
	@Override
	public Set<Capitalist> getElements() {
		Set<Capitalist> elements = new HashSet<Capitalist>();
		for (Capitalist fatCats : hierarchyCollection.keySet()) {
			elements.add(fatCats);
			elements.addAll(getChildrenEditable((FatCat) fatCats));
		}
		return elements;
	}

	/**
	 * @return all parent elements in the hierarchy as a set that can be
	 *         modified, or an empty set if no parents have been added to the
	 *         hierarchy
	 */
	public Set<FatCat> getParentsEditable() {
		if (hierarchyCollection.keySet() == null) {
			Set<FatCat> emptySet = new HashSet<FatCat>();
			return emptySet;
		}

		return hierarchyCollection.keySet();
	}

	/**
	 * @return all parent elements in the hierarchy, or an empty set if no
	 *         parents have been added to the hierarchy
	 */
	@Override
	public Set<FatCat> getParents() {
		Set<FatCat> parentSet = new HashSet<FatCat>();
		if (hierarchyCollection.keySet() == null) {
			return parentSet;
		}

		parentSet.addAll(hierarchyCollection.keySet());
		return parentSet;
	}

	/**
	 * @param fatCat
	 *            the parent whose children need to be returned
	 * @return The actual set of children from the hierarchy that have the given
	 *         parent as a direct parent, or an empty set if the parent is not
	 *         present in the hierarchy or if there are no children for the
	 *         given parent
	 */
	private Set<Capitalist> getChildrenEditable(FatCat fatCat) {
		if (!has(fatCat)) {
			Set<Capitalist> emptySet = new HashSet<Capitalist>();
			return emptySet;
		}
		return hierarchyCollection.get(fatCat);
	}

	/**
	 * @param fatCat
	 *            the parent whose children need to be returned
	 * @return all elements in the hierarchy that have the given parent as a
	 *         direct parent, or an empty set if the parent is not present in
	 *         the hierarchy or if there are no children for the given parent
	 */
	@Override
	public Set<Capitalist> getChildren(FatCat fatCat) {
		Set<Capitalist> coppiedChildren = new HashSet<Capitalist>();
		if (!has(fatCat)) {
			return coppiedChildren;
		}
		coppiedChildren.addAll(hierarchyCollection.get(fatCat));
		return coppiedChildren;
	}

	/**
	 * @return a map in which the keys represent the parent elements in the
	 *         hierarchy, and the each value is a set of the direct children of
	 *         the associate parent, or an empty map if the hierarchy is empty.
	 */
	@Override
	public Map<FatCat, Set<Capitalist>> getHierarchy() {
		Map<FatCat, Set<Capitalist>> hierarcyCopy = new HashMap();
		for(FatCat fatCats : getParents()) {
			hierarcyCopy.put(fatCats, getChildren(fatCats));
		}
		return hierarcyCopy;
	}

	/**
	 * @param capitalist
	 * @return the parent chain of the given element, starting with its direct
	 *         parent, then its parent's parent, etc, or an empty list if the
	 *         given element has no parent or if its parent is not in the
	 *         hierarchy
	 */
	@Override
	public List<FatCat> getParentChain(Capitalist capitalist) {
		List<FatCat> parentChain = new ArrayList<FatCat>();
		if (capitalist != null) {
			if(has(capitalist.getParent())) {
				Capitalist lineageMember = capitalist;
				while(lineageMember.hasParent()) {
					lineageMember = lineageMember.getParent();
					parentChain.add((FatCat) lineageMember);
				}
			}
			
//			FatCat parent = capitalist.getParent();
//			if (!has(parent)) {
//				return parentChain;
//			}
//			while (has(parent)) {
//				parentChain.add((FatCat) parent);
//				parent = parent.getParent();
//			}
		}

		return parentChain;
	}
}
