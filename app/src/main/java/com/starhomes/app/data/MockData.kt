package com.starhomes.app.data

object MockData {

    val USER = User(
        name = "Rafael Almeida",
        email = "rafael.almeida@email.com",
        phone = "11 98765-4321",
        avatar = "https://picsum.photos/seed/rafael/200"
    )

    val PREFERENCES = Preferences(
        profileType = ProfileType.PROFESSIONAL,
        priceRange = Pair(1200, 1700),
        priorities = Priorities(schools = false, transport = true, safety = true)
    )

    val NEIGHBORHOODS = listOf(
        Neighborhood(
            id = "kensington",
            name = "Kensington",
            description = "Bairro familiar, próximo a escolas e parques.",
            priceRange = "£1,200 - £1,500/mês",
            image = "https://picsum.photos/seed/kensington-london/800",
            center = NeighborhoodCenter(lat = 51.5015, lng = -0.1934),
            properties = listOf(
                Property(
                    id = "k1", type = "Apartamento", bedrooms = 2, price = 1200,
                    image = "https://picsum.photos/seed/k1-apartment/800",
                    lat = 51.5030, lng = -0.1910,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/2724749/pexels-photo-2724749.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/1454806/pexels-photo-1454806.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed2", "Quarto 2", 72f, 25f, "https://images.pexels.com/photos/3753435/pexels-photo-3753435.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/1485031/pexels-photo-1485031.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                ),
                Property(
                    id = "k2", type = "Studio moderno", bedrooms = 1, price = 1350,
                    image = "https://picsum.photos/seed/k2-studio/800",
                    lat = 51.5000, lng = -0.1950,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/276724/pexels-photo-276724.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/1080721/pexels-photo-1080721.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/853316/pexels-photo-853316.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                ),
                Property(
                    id = "k3", type = "Casa 3 quartos", bedrooms = 3, price = 1500,
                    image = "https://picsum.photos/seed/k3-house/800",
                    lat = 51.5025, lng = -0.1960,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/1643383/pexels-photo-1643383.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/3926542/pexels-photo-3926542.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/775219/pexels-photo-775219.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed2", "Quarto 2", 72f, 25f, "https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/2062431/pexels-photo-2062431.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                )
            )
        ),
        Neighborhood(
            id = "camden",
            name = "Camden",
            description = "Área jovem, próxima a transporte público.",
            priceRange = "£1,400 - £1,700/mês",
            image = "https://picsum.photos/seed/camden-london/800",
            center = NeighborhoodCenter(lat = 51.5394, lng = -0.1426),
            properties = listOf(
                Property(
                    id = "c1", type = "Loft industrial", bedrooms = 1, price = 1400,
                    image = "https://picsum.photos/seed/c1-loft/800",
                    lat = 51.5410, lng = -0.1400,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/1571458/pexels-photo-1571458.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/667838/pexels-photo-667838.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/279746/pexels-photo-279746.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/2251206/pexels-photo-2251206.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                ),
                Property(
                    id = "c2", type = "Apartamento 2 quartos", bedrooms = 2, price = 1650,
                    image = "https://picsum.photos/seed/c2-apartment/800",
                    lat = 51.5380, lng = -0.1450,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/1918291/pexels-photo-1918291.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/2635038/pexels-photo-2635038.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/1329711/pexels-photo-1329711.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed2", "Quarto 2", 72f, 25f, "https://images.pexels.com/photos/262048/pexels-photo-262048.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/3288102/pexels-photo-3288102.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                )
            )
        ),
        Neighborhood(
            id = "shoreditch",
            name = "Shoreditch",
            description = "Vibrante e artístico, cheio de bares e galerias.",
            priceRange = "£1,500 - £2,200/mês",
            image = "https://picsum.photos/seed/shoreditch-london/800",
            center = NeighborhoodCenter(lat = 51.5229, lng = -0.0769),
            properties = listOf(
                Property(
                    id = "s1", type = "Apartamento de designer", bedrooms = 2, price = 2100,
                    image = "https://picsum.photos/seed/s1-designer/800",
                    lat = 51.5250, lng = -0.0790,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/271816/pexels-photo-271816.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/5591662/pexels-photo-5591662.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/6434622/pexels-photo-6434622.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed2", "Quarto 2", 72f, 25f, "https://images.pexels.com/photos/6585759/pexels-photo-6585759.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/6621472/pexels-photo-6621472.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                ),
                Property(
                    id = "s2", type = "Estúdio com varanda", bedrooms = 1, price = 1750,
                    image = "https://picsum.photos/seed/s2-studio/800",
                    lat = 51.5215, lng = -0.0750,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/259962/pexels-photo-259962.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/189333/pexels-photo-189333.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/2029722/pexels-photo-2029722.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/6782476/pexels-photo-6782476.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                ),
                Property(
                    id = "s3", type = "Cobertura", bedrooms = 3, price = 2200,
                    image = "https://picsum.photos/seed/s3-penthouse/800",
                    lat = 51.5230, lng = -0.0720,
                    floorPlan = FloorPlan(
                        image = "https://i.ibb.co/qCb7sYc/floorplan-blueprint.png",
                        rooms = listOf(
                            Room("living", "Sala de Estar", 30f, 65f, "https://images.pexels.com/photos/439227/pexels-photo-439227.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("kitchen", "Cozinha", 68f, 78f, "https://images.pexels.com/photos/6284227/pexels-photo-6284227.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed1", "Quarto 1", 28f, 25f, "https://images.pexels.com/photos/3622479/pexels-photo-3622479.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bed2", "Quarto 2", 72f, 25f, "https://images.pexels.com/photos/545014/pexels-photo-545014.jpeg?auto=compress&cs=tinysrgb&w=800"),
                            Room("bath", "Banheiro", 50f, 25f, "https://images.pexels.com/photos/6588046/pexels-photo-6588046.jpeg?auto=compress&cs=tinysrgb&w=800")
                        )
                    )
                )
            )
        )
    )

    fun allProperties(): List<Property> = NEIGHBORHOODS.flatMap { it.properties }
    fun findProperty(id: String): Property? = allProperties().find { it.id == id }
    fun findNeighborhoodByProperty(propertyId: String): Neighborhood? =
        NEIGHBORHOODS.find { n -> n.properties.any { it.id == propertyId } }
}
