package org.lucci.lmu.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import toools.collections.Collections;
import toools.collections.Filter;


/*
 * Created on Oct 10, 2004
 */

/**
 * @author luc.hogie
 */
public class Relations
{
	public static Collection<Relation> findAssociations(Collection<Relation> relations)
	{
		return Collections.filter(relations, new Filter.ClassFilter(AssociationRelation.class));
	}

	public static Collection<Relation> findInheritances(Collection<Relation> relations)
	{
		return Collections.filter(relations, new Filter.ClassFilter(InheritanceRelation.class));
	}

	public static List<Relation> findRelationsDeclaredBy(Entity entity, IModel model)
	{
		List<Relation> rels = new ArrayList<Relation>();

		for (Relation rel : model.getRelations())
		{
			if (entity.declareRelation(rel))
			{
				rels.add(rel);
			}
		}

		return rels;
	}

	public static Collection<Entity> findEntitiesImpliedIn(Collection<Relation> relations)
	{
		Collection<Entity> entities = new HashSet<Entity>();

		for (Relation relation : relations)
		{
			entities.add(relation.getHeadEntity());
			entities.add(relation.getTailEntity());
		}

		return entities;
	}

	public static Set<Relation> findRelationsInvolving(Entity entity, Collection<Relation> relations)
	{
		Set<Relation> rels = new HashSet<Relation>();

		for (Relation relation : relations)
		{
			if (relation.involve(entity))
			{
				rels.add(relation);
			}
		}

		return rels;
	}

	public static Relation findRelation(Set<Relation> relations, Class<?> c, Entity tail, Entity head)
	{
		for (Relation r : relations)
		{
			if (r.getTailEntity() == tail && r.getHeadEntity() == head && r.getClass() == c)
			{
				return r;
			}
		}

		return null;
	}

	public static Set<Relation> findRelationsInvolving(Set<Relation> relations, Entity e1, Entity e2)
	{
		Set<Relation> l1 = findRelationsInvolving(e1, relations);
		Set<Relation> l2 = findRelationsInvolving(e2, relations);
		return (Set<Relation>) Collections.intersection(l1, l2);
	}

	public static Set<AssociationRelation> convertAttributesToCompositions(Set<Attribute> attributes, Entity entity, Model model)
	{
		Set<AssociationRelation> newRelations = new HashSet<AssociationRelation>();

		// copy the entities into a vector because some entities might be added
		// to the map
		// if we don't do that, we get a ConcurentAccessException

		for (Attribute attribute : attributes)
		{
			if (attributes.contains(attribute))
			{
				Entity attributeType = attribute.getType();

				// it is likely that some entities have been previously removed
				// from the diagram
				// (for cleaning purposes for example), we need to inject some
				// of them
				if (!model.getEntities().contains(attributeType))
				{
					model.getEntities().add(attributeType);
				}

				AssociationRelation rel = new AssociationRelation(attributeType, entity);
				rel.setType(AssociationRelation.TYPE.COMPOSITION);
				rel.setCardinality("1");
				rel.setLabel(attribute.getName());
				model.getRelations().add(rel);
				newRelations.add(rel);
				entity.getAttributes().remove(attribute);
			}
		}

		return newRelations;
	}



	public static void removeAssociationsCardinalities(Collection<AssociationRelation> relations)
	{
		for (AssociationRelation rel : relations)
		{
			rel.setCardinality(null);
		}
	}

	public static void removeAssociationsLabels(Collection<AssociationRelation> relations)
	{
		for (AssociationRelation rel : relations)
		{
			rel.setLabel(null);
		}
	}

	public static void convertInheritanceToComposition(Collection<InheritanceRelation> relations, Model model)
	{
		for (InheritanceRelation irel : relations)
		{
			AssociationRelation arel = new AssociationRelation(irel.getSuperEntity(), irel.getSubEntity());
			arel.setType(AssociationRelation.TYPE.COMPOSITION);
			arel.setLabel("extends");
			arel.setCardinality("1");

			irel.getSubEntity().getOperations().addAll(irel.getSuperEntity().getOperations());
			irel.getSuperEntity().getOperations().clear();

			model.getRelations().remove(irel);
			model.getRelations().add(arel);
		}
	}
}
