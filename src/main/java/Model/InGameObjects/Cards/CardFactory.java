package Model.InGameObjects.Cards;
import Enum.*;
import Model.InGameObjects.Abilities.LeaderAbilities.Emhyr.*;
import Model.InGameObjects.Abilities.LeaderAbilities.Eredin.*;
import Model.InGameObjects.Abilities.LeaderAbilities.Foltest.*;
import Model.InGameObjects.Abilities.LeaderAbilities.Francesca.*;
import Model.InGameObjects.Abilities.SpellAbilities.*;
import Model.InGameObjects.Abilities.UnitAbilities.*;
import Model.InGameObjects.Abilities.LeaderAbilities.Skellige.CrachAnCraite;
import Model.InGameObjects.Abilities.LeaderAbilities.Skellige.KingBran;
import Model.InGameObjects.Abilities.LeaderAbilities.SevenKingdoms.*;

import java.util.ArrayList;

public class CardFactory {

    private static int numberOfCloneInvocation = 0;
    private static ArrayList<Card> allCards = new ArrayList<>();

    private static ArrayList<Card> cheatCards = new ArrayList<>();

    public static Card getCopiedCardByName(CardNames cardName) {
        for (Card card : allCards) {
            if (card.getCardName().equals(cardName))
                return card.deepCopy();
        }

        return null;
    }

    public static Card getCopiedCheatCardByName(CardNames cardName) {
        for (Card card : cheatCards) {
            if (card.getCardName().equals(cardName))
                return card.deepCopy();
        }

        return null;
    }

    public static ArrayList<Card> cloneAllCardsOfFaction(FactionName factionName) {
        ArrayList<Card> factionInventory = new ArrayList<>();
        for (Card card :allCards) {
            if (card.getCardName().getFactionNameAsEnum().equals(FactionName.Specials)) {
                if (numberOfCloneInvocation == 0)
                    factionInventory.add(card);
                else
                    factionInventory.add(card.deepCopy());
            }
        }

        for (Card card :allCards) {
            if (card.getCardName().getFactionNameAsEnum().equals(FactionName.Neutrals)) {
                if (numberOfCloneInvocation == 0)
                    factionInventory.add(card);
                else
                    factionInventory.add(card.deepCopy());
            }
        }

        for (Card card :allCards) {
            if (card.getCardName().getFactionNameAsEnum().equals(factionName)) {
                factionInventory.add(card);
            }
        }
        numberOfCloneInvocation++;
        return factionInventory;
    }

    public static void createAllCards() {
        createCheatCards();
        createSpecialCards(); // same with spell cards.
        createNeutralUnitCards();
        createMonstersDeckCards();
        createNilfgaardDeckCards();
        createNorthernRealmsDeckCards();
        createScoiaTaelDeckCards();
        createSkelligeDeckCards();
        createSevenKingdomsCards();
        giveInitialStateToCards();
    }

    private static void giveInitialStateToCards() {
        for (Card card : allCards) {
            card.setPreviousState(CardStates.DES_PRE_GAME_MENU);
            card.setCurrentState(CardStates.DES_PRE_GAME_MENU);
        }
    }

    private static void createCheatCards() {
        UnitCard homelander = new UnitCard(CardNames.HOMELANDER, CardPlace.FRIENDLY_CLOSE,
                111, true, new ArrayList<CardAbility>());
        cheatCards.add(homelander);

        UnitCard butcher = new UnitCard(CardNames.BUTCHER, CardPlace.FRIENDLY_CLOSE,
                111, true, new ArrayList<CardAbility>(){{
                    add(new UnitCommandersHornAbility());
        }});
        cheatCards.add(butcher);
    }

    private static void createSpecialCards() {
        SpellCard decoy1 = new SpellCard(CardNames.DECOY, CardPlace.DECOY_PLACE,
            new ArrayList<CardAbility>(){{
                add(new SpellDecoyAbility());
            }});
        allCards.add(decoy1);

        SpellCard decoy2 = new SpellCard(CardNames.DECOY, CardPlace.DECOY_PLACE,
                new ArrayList<CardAbility>(){{
                    add(new SpellDecoyAbility());
                }});
        allCards.add(decoy2);

        SpellCard decoy3 = new SpellCard(CardNames.DECOY, CardPlace.DECOY_PLACE,
                new ArrayList<CardAbility>(){{
                    add(new SpellDecoyAbility());
                }});
        allCards.add(decoy3);

        SpellCard horn1 = new SpellCard(CardNames.HORN, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellCommandersHornAbility());
                }});
        allCards.add(horn1);

        SpellCard horn2 = new SpellCard(CardNames.HORN, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellCommandersHornAbility());
                }});
        allCards.add(horn2);

        SpellCard horn3 = new SpellCard(CardNames.HORN, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellCommandersHornAbility());
                }});
        allCards.add(horn3);

        SpellCard mardroeme1 = new SpellCard(CardNames.MARDROEME, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellMardroemeAbility());
                }});
        allCards.add(mardroeme1);

        SpellCard mardroeme2 = new SpellCard(CardNames.MARDROEME, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellMardroemeAbility());
                }});
        allCards.add(mardroeme2);

        SpellCard mardroeme3 = new SpellCard(CardNames.MARDROEME, CardPlace.HORN_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellMardroemeAbility());
                }});
        allCards.add(mardroeme3);

        SpellCard scorch1 = new SpellCard(CardNames.SCORCH, CardPlace.FRIENDLY_DISCARD_PILE_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellScorchAbility());
                }});
        allCards.add(scorch1);

        SpellCard scorch2 = new SpellCard(CardNames.SCORCH, CardPlace.FRIENDLY_DISCARD_PILE_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellScorchAbility());
                }});
        allCards.add(scorch2);

        SpellCard scorch3 = new SpellCard(CardNames.SCORCH, CardPlace.FRIENDLY_DISCARD_PILE_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellScorchAbility());
                }});
        allCards.add(scorch3);

        SpellCard clearWeather1 = new SpellCard(CardNames.CLEAR_WEATHER, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellClearWeatherAbility());
                }});
        allCards.add(clearWeather1);

        SpellCard clearWeather2 = new SpellCard(CardNames.CLEAR_WEATHER, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellClearWeatherAbility());
                }});
        allCards.add(clearWeather2);

        SpellCard clearWeather3 = new SpellCard(CardNames.CLEAR_WEATHER, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellClearWeatherAbility());
                }});
        allCards.add(clearWeather3);

        SpellCard fog1 = new SpellCard(CardNames.FOG, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellImpenetrableFogAbility());
                }});
        allCards.add(fog1);

        SpellCard fog2 = new SpellCard(CardNames.FOG, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellImpenetrableFogAbility());
                }});
        allCards.add(fog2);

        SpellCard fog3 = new SpellCard(CardNames.FOG, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellImpenetrableFogAbility());
                }});
        allCards.add(fog3);

        SpellCard frost1 = new SpellCard(CardNames.FROST, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellBitingFrostAbility());
                }});
        allCards.add(frost1);

        SpellCard frost2 = new SpellCard(CardNames.FROST, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellBitingFrostAbility());
                }});
        allCards.add(frost2);

        SpellCard frost3 = new SpellCard(CardNames.FROST, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellBitingFrostAbility());
                }});
        allCards.add(frost3);

        SpellCard rain1 = new SpellCard(CardNames.RAIN, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellTorrentialRainAbility());
                }});
        allCards.add(rain1);

        SpellCard rain2 = new SpellCard(CardNames.RAIN, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellTorrentialRainAbility());
                }});
        allCards.add(rain2);

        SpellCard rain3 = new SpellCard(CardNames.RAIN, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellTorrentialRainAbility());
                }});
        allCards.add(rain3);

        SpellCard storm1 = new SpellCard(CardNames.STORM, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellSkelligeStormAbility());
                }});
        allCards.add(storm1);

        SpellCard storm2 = new SpellCard(CardNames.STORM, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellSkelligeStormAbility());
                }});
        allCards.add(storm2);

        SpellCard storm3 = new SpellCard(CardNames.STORM, CardPlace.WEATHER_CARDS_AREA,
                new ArrayList<CardAbility>(){{
                    add(new SpellSkelligeStormAbility());
                }});
        allCards.add(storm3);
    }

    private static void createNeutralUnitCards() {
        UnitCard cowTransformed = new UnitCard(CardNames.COW_TRANSFORMED, CardPlace.FRIENDLY_CLOSE,
                8, false, new ArrayList<CardAbility>());

        UnitCard cow = new UnitCard(CardNames.COW, CardPlace.FRIENDLY_RANGED,
                0, false, new ArrayList<CardAbility>(){{
            add(new UnitTransformerAbility(cowTransformed));
        }});
        allCards.add(cow);

        UnitCard ciri = new UnitCard(CardNames.CIRI, CardPlace.FRIENDLY_CLOSE,
                15, true, new ArrayList<CardAbility>());
        allCards.add(ciri);

        UnitCard dandelion = new UnitCard(CardNames.DANDELION, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitCommandersHornAbility());
        }});
        allCards.add(dandelion);

        UnitCard emielRegis = new UnitCard(CardNames.EMIEL_REGIS, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(emielRegis);

        UnitCard gaunterOdimm = new UnitCard(CardNames.GAUNTER_ODIMM, CardPlace.FRIENDLY_SIEGE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GAUNTER_ODIMM));
        }});
        allCards.add(gaunterOdimm);

        UnitCard gaunterOdimmDarkness1 = new UnitCard(CardNames.GAUNTER_ODIMM_DARKNESS, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GAUNTER_ODIMM_DARKNESS));
        }});
        allCards.add(gaunterOdimmDarkness1);

        UnitCard gaunterOdimmDarkness2 = new UnitCard(CardNames.GAUNTER_ODIMM_DARKNESS, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GAUNTER_ODIMM_DARKNESS));
        }});
        allCards.add(gaunterOdimmDarkness2);

        UnitCard gaunterOdimmDarkness3 = new UnitCard(CardNames.GAUNTER_ODIMM_DARKNESS, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GAUNTER_ODIMM_DARKNESS));
        }});
        allCards.add(gaunterOdimmDarkness3);

        UnitCard geralt = new UnitCard(CardNames.GERALT, CardPlace.FRIENDLY_CLOSE,
                15, true, new ArrayList<CardAbility>());
        allCards.add(geralt);

        UnitCard mysteriousElf = new UnitCard(CardNames.MYSTERIOUS_ELF, CardPlace.OPPONENT_CLOSE,
                0, true, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(mysteriousElf);

        UnitCard olgierd = new UnitCard(CardNames.OLGIERD, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
            add(new UnitAgileAbility());
        }});
        allCards.add(olgierd);

        UnitCard triss = new UnitCard(CardNames.TRISS, CardPlace.FRIENDLY_CLOSE,
                7, true, new ArrayList<CardAbility>());
        allCards.add(triss);

        UnitCard vessemir = new UnitCard(CardNames.VESSEMIR, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(vessemir);

        UnitCard villen = new UnitCard(CardNames.VILLEN, CardPlace.FRIENDLY_CLOSE,
                7, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(villen);

        UnitCard yennefer = new UnitCard(CardNames.YENNEFER, CardPlace.FRIENDLY_RANGED,
                7, true, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(yennefer);

        UnitCard zoltan = new UnitCard(CardNames.ZOLTAN, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(zoltan);
    }

    private static void createMonstersDeckCards() {
        LeaderCard eredin1 = new LeaderCard(CardNames.EREDIN_KING_OF_WILDHUNT, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
                    add(new KingOfTheWildHunt());
        }});
        allCards.add(eredin1);

        LeaderCard eredin2 = new LeaderCard(CardNames.EREDIN_COMMANDER_OF_RED_RIDERS, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new CommanderOfTheRedRiders());
        }});
        allCards.add(eredin2);

        LeaderCard eredin3 = new LeaderCard(CardNames.EREDIN_DESTROYER_OF_WORLDS, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new DestroyerOfWorlds());
        }});
        allCards.add(eredin3);

        LeaderCard eredin4 = new LeaderCard(CardNames.EREDIN_BRINGER_OF_DEATH, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new BringerOfDeath());
        }});
        allCards.add(eredin4);

        LeaderCard eredin5 = new LeaderCard(CardNames.EREDIN_TREACHEROUS, CardPlace.FRIENDLY_LEADER,
                true, new ArrayList<CardAbility>(){{
            add(new TheTreacherous());
        }});
        allCards.add(eredin5);

        UnitCard arachas_1 = new UnitCard(CardNames.ARACHAS_1, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
                    add(new UnitMusterAbility(CardMusterTypes.ARACHAS));
        }});
        allCards.add(arachas_1);

        UnitCard arachas_2 = new UnitCard(CardNames.ARACHAS_2, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ARACHAS));
        }});
        allCards.add(arachas_2);

        UnitCard arachas_3 = new UnitCard(CardNames.ARACHAS_3, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ARACHAS));
        }});
        allCards.add(arachas_3);

        UnitCard arachasBehemoth = new UnitCard(CardNames.ARACHAS_BEHEMOTH, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ARACHAS));
        }});
        allCards.add(arachasBehemoth);

        UnitCard bruxa = new UnitCard(CardNames.BRUXA, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.VAMPIRE));
        }});
        allCards.add(bruxa);

        UnitCard celaenoHarpy = new UnitCard(CardNames.CELAENO_HARPY, CardPlace.FRIENDLY_CLOSE_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(celaenoHarpy);

        UnitCard cockatrice = new UnitCard(CardNames.COCKATRICE, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(cockatrice);

        UnitCard draug = new UnitCard(CardNames.DRAUG, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(draug);

        UnitCard earthElemental = new UnitCard(CardNames.EARTH_ELEMENTAL, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(earthElemental);

        UnitCard ekimmara = new UnitCard(CardNames.EKIMMARA, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.VAMPIRE));
        }});
        allCards.add(ekimmara);

        UnitCard endrega = new UnitCard(CardNames.ENDREGA, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(endrega);

        UnitCard fiend = new UnitCard(CardNames.FIEND, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(fiend);

        UnitCard fireElemental = new UnitCard(CardNames.FIRE_ELEMENTAL, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(fireElemental);

        UnitCard fleder = new UnitCard(CardNames.FLEDER, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.VAMPIRE));
        }});
        allCards.add(fleder);

        UnitCard foglet = new UnitCard(CardNames.FOGLET, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>());
        allCards.add(foglet);

        UnitCard forktail = new UnitCard(CardNames.FORKTAIL, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(forktail);

        UnitCard frightener = new UnitCard(CardNames.FRIGHTENER, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(frightener);

        UnitCard iceGiant = new UnitCard(CardNames.ICE_GIANT, CardPlace.FRIENDLY_SIEGE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(iceGiant);

        UnitCard gargoyle = new UnitCard(CardNames.GARGOYLE, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(gargoyle);

        UnitCard garakin = new UnitCard(CardNames.GARKAIN, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.VAMPIRE));
        }});
        allCards.add(garakin);

        UnitCard ghoul1 = new UnitCard(CardNames.GHOUL_1, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GHOUL));
        }});
        allCards.add(ghoul1);

        UnitCard ghoul2 = new UnitCard(CardNames.GHOUL_2, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GHOUL));
        }});
        allCards.add(ghoul2);

        UnitCard ghoul3 = new UnitCard(CardNames.GHOUL_3, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.GHOUL));
        }});
        allCards.add(ghoul3);

        UnitCard gravehag = new UnitCard(CardNames.GRAVEHAG, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>());
        allCards.add(gravehag);

        UnitCard griffin = new UnitCard(CardNames.GRYFFIN, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>());
        allCards.add(griffin);

        UnitCard harpy = new UnitCard(CardNames.HARPY, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
                    add(new UnitAgileAbility());
        }});
        allCards.add(harpy);

        UnitCard imlerith = new UnitCard(CardNames.IMLERITH, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(imlerith);

        UnitCard katakan = new UnitCard(CardNames.KATAKAN, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.VAMPIRE));
        }});
        allCards.add(katakan);

        UnitCard kayran = new UnitCard(CardNames.KAYRAN, CardPlace.FRIENDLY_CLOSE_RANGED,
                8, true, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
            add(new UnitAgileAbility());
        }});
        allCards.add(kayran);

        UnitCard leshen = new UnitCard(CardNames.LESHEN, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(leshen);

        UnitCard plaugeMaiden = new UnitCard(CardNames.PLAUGE_MAIDEN, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(plaugeMaiden);

        UnitCard nekker1 = new UnitCard(CardNames.NEKKER_1, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.NEKKER));
        }});
        allCards.add(nekker1);

        UnitCard nekker2 = new UnitCard(CardNames.NEKKER_2, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.NEKKER));
        }});
        allCards.add(nekker2);

        UnitCard nekker3 = new UnitCard(CardNames.NEKKER_3, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.NEKKER));
        }});
        allCards.add(nekker3);

        UnitCard botchling = new UnitCard(CardNames.BOTCHLING, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(botchling);

        UnitCard toad = new UnitCard(CardNames.TOAD, CardPlace.FRIENDLY_RANGED,
                7, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(toad);

        UnitCard werewolf = new UnitCard(CardNames.WEREWOLF, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(werewolf);

        UnitCard witchVelen1 = new UnitCard(CardNames.WITCH_VELEN_1, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.WITCH_VELEN));
        }});
        allCards.add(witchVelen1);

        UnitCard witchVelen2 = new UnitCard(CardNames.WITCH_VELEN_2, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.WITCH_VELEN));
        }});
        allCards.add(witchVelen2);

        UnitCard witchVelen3 = new UnitCard(CardNames.WITCH_VELEN_3, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.WITCH_VELEN));
        }});
        allCards.add(witchVelen3);

        UnitCard wyvern = new UnitCard(CardNames.WYVERN, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(wyvern);
    }

    private static void createNilfgaardDeckCards() {
        LeaderCard emhyr1 = new LeaderCard(CardNames.EMHYR_EMPEROR_OF_NILFGAARD, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
                    add(new EmperorOfNilfgaard());
        }});
        allCards.add(emhyr1);

        LeaderCard emhyr2 = new LeaderCard(CardNames.EMHYR_HIS_IMPERIAL_MAJESTY, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new HisImperialMajesty());
        }});
        allCards.add(emhyr2);

        LeaderCard emhyr3 = new LeaderCard(CardNames.EMHYR_THE_RELENTLESS, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new TheRelentless());
        }});
        allCards.add(emhyr3);

        LeaderCard emhyr4 = new LeaderCard(CardNames.EMHYR_INVADER_OF_THE_NORTH, CardPlace.FRIENDLY_LEADER,
                true, new ArrayList<CardAbility>(){{
            add(new InvaderOfTheNorth());
        }});
        allCards.add(emhyr4);

        LeaderCard emhyr5 = new LeaderCard(CardNames.EMHYR_THE_WHITE_FLAME, CardPlace.FRIENDLY_LEADER,
                true, new ArrayList<CardAbility>(){{
            add(new TheWhiteFlame());
        }});
        allCards.add(emhyr5);

        UnitCard albrich = new UnitCard(CardNames.ALBRICH, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(albrich);

        UnitCard auxiliaryArcher1 = new UnitCard(CardNames.AUXILIARY_ARCHER_1, CardPlace.FRIENDLY_RANGED,
                1, false, new ArrayList<CardAbility>(){{
                    add (new UnitMedicAbility());
        }});
        allCards.add(auxiliaryArcher1);

        UnitCard auxiliaryArcher2 = new UnitCard(CardNames.AUXILIARY_ARCHER_2, CardPlace.FRIENDLY_RANGED,
                1, false, new ArrayList<CardAbility>(){{
            add (new UnitMedicAbility());
        }});
        allCards.add(auxiliaryArcher2);

        UnitCard assire = new UnitCard(CardNames.ASSIRE, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(assire);

        UnitCard blackArcher1 = new UnitCard(CardNames.BLACK_ARCHER_1, CardPlace.FRIENDLY_RANGED,
                10, false, new ArrayList<CardAbility>());
        allCards.add(blackArcher1);

        UnitCard blackArcher2 = new UnitCard(CardNames.BLACK_ARCHER_2, CardPlace.FRIENDLY_RANGED,
                10, false, new ArrayList<CardAbility>());
        allCards.add(blackArcher2);

        UnitCard cahir = new UnitCard(CardNames.CAHIR, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(cahir);

        UnitCard cynthia = new UnitCard(CardNames.CYNTHIA, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(cynthia);

        UnitCard fringilla = new UnitCard(CardNames.FRINGILLA, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(fringilla);

        UnitCard heavyScorpion = new UnitCard(CardNames.HEAVY_SCORPION, CardPlace.FRIENDLY_SIEGE,
                10, false, new ArrayList<CardAbility>());
        allCards.add(heavyScorpion);

        UnitCard imperaGuard1 = new UnitCard(CardNames.IMPERA_GUARD, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.IMPERA_GUARDS));
        }});
        allCards.add(imperaGuard1);

        UnitCard imperaGuard2 = new UnitCard(CardNames.IMPERA_GUARD, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.IMPERA_GUARDS));
        }});
        allCards.add(imperaGuard2);

        UnitCard imperaGuard3 = new UnitCard(CardNames.IMPERA_GUARD, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.IMPERA_GUARDS));
        }});
        allCards.add(imperaGuard3);

        UnitCard imperaGuard4 = new UnitCard(CardNames.IMPERA_GUARD, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.IMPERA_GUARDS));
        }});
        allCards.add(imperaGuard4);

        UnitCard letho = new UnitCard(CardNames.LETHO, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(letho);

        UnitCard menno = new UnitCard(CardNames.MENNO, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>(){{
            add (new UnitMedicAbility());
        }});
        allCards.add(menno);

        UnitCard morvran = new UnitCard(CardNames.MORVRAN, CardPlace.FRIENDLY_SIEGE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(morvran);

        UnitCard morteisen = new UnitCard(CardNames.MORTEISEN, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>());
        allCards.add(morteisen);

        UnitCard nauzicaaCavalry1 = new UnitCard(CardNames.NAUZICAA_CAVALRY, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.NAUSICAA_CAVALRY));
        }});
        allCards.add(nauzicaaCavalry1);

        UnitCard nauzicaaCavalry2 = new UnitCard(CardNames.NAUZICAA_CAVALRY, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.NAUSICAA_CAVALRY));
        }});
        allCards.add(nauzicaaCavalry2);

        UnitCard nauzicaaCavalry3 = new UnitCard(CardNames.NAUZICAA_CAVALRY, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.NAUSICAA_CAVALRY));
        }});
        allCards.add(nauzicaaCavalry3);

        UnitCard puttkemmer = new UnitCard(CardNames.PUTTKAMMER, CardPlace.FRIENDLY_RANGED,
                3, false, new ArrayList<CardAbility>());
        allCards.add(puttkemmer);

        UnitCard rainfaarn = new UnitCard(CardNames.RAINFAARN, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(rainfaarn);

        UnitCard renuald = new UnitCard(CardNames.RENUALD, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>());
        allCards.add(renuald);

        UnitCard rottenMangonel = new UnitCard(CardNames.ROTTEN_MANGONEL, CardPlace.FRIENDLY_SIEGE,
                3, false, new ArrayList<CardAbility>());
        allCards.add(rottenMangonel);

        UnitCard shilard = new UnitCard(CardNames.SHILARD, CardPlace.OPPONENT_CLOSE,
                7, false, new ArrayList<CardAbility>(){{
            add (new UnitSpyAbility());
        }});
        allCards.add(shilard);

        UnitCard siegeEngineer = new UnitCard(CardNames.SIEGE_ENGINEER, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(siegeEngineer);

        UnitCard siegeTechnician = new UnitCard(CardNames.SIEGE_TECHNICIAN, CardPlace.FRIENDLY_SIEGE,
                0, false, new ArrayList<CardAbility>(){{
            add (new UnitMedicAbility());
        }});
        allCards.add(siegeTechnician);

        UnitCard stefan = new UnitCard(CardNames.STEFAN, CardPlace.OPPONENT_CLOSE,
                9, false, new ArrayList<CardAbility>(){{
            add (new UnitSpyAbility());
        }});
        allCards.add(stefan);

        UnitCard sweers = new UnitCard(CardNames.SWEERS, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(sweers);

        UnitCard tigbor = new UnitCard(CardNames.TIGBOR, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(tigbor);

        UnitCard vanhemer = new UnitCard(CardNames.VANHEMER, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(vanhemer);

        UnitCard vattier = new UnitCard(CardNames.VATTIER, CardPlace.OPPONENT_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add (new UnitSpyAbility());
        }});
        allCards.add(vattier);

        UnitCard vreemde = new UnitCard(CardNames.VREEMDE, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>());
        allCards.add(vreemde);

        UnitCard youngEmissary1 = new UnitCard(CardNames.YOUNG_EMISSARY_1, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.YOUNG_EMISSARIES));
        }});
        allCards.add(youngEmissary1);

        UnitCard youngEmissary2 = new UnitCard(CardNames.YOUNG_EMISSARY_2, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add (new UnitTightBondAbility(CardTightBTypes.YOUNG_EMISSARIES));
        }});
        allCards.add(youngEmissary2);

        UnitCard scorpion = new UnitCard(CardNames.SCORPION, CardPlace.FRIENDLY_SIEGE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(scorpion);
    }

    private static void createNorthernRealmsDeckCards() {
        LeaderCard foltest1 = new LeaderCard(CardNames.FOLTEST_LORD_COMMANDER_OF_THE_NORTH, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
                    add(new LordCommanderOfTheNorth());
        }});
        allCards.add(foltest1);

        LeaderCard foltest2 = new LeaderCard(CardNames.FOLTEST_KING_OF_TEMARIA, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new KingOfTemeria());
        }});
        allCards.add(foltest2);

        LeaderCard foltest3 = new LeaderCard(CardNames.FOLTEST_THE_STEEL_FORGED, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new TheSteelForged());
        }});
        allCards.add(foltest3);

        LeaderCard foltest4 = new LeaderCard(CardNames.FOLTEST_THE_SIEGE_MASTER, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new TheSiegeMaster());
        }});
        allCards.add(foltest4);

        LeaderCard foltest5 = new LeaderCard(CardNames.FOLTEST_SON_OF_MEDELL, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new SonOfMedell());
        }});
        allCards.add(foltest5);

        UnitCard balista = new UnitCard(CardNames.BALLISTA, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(balista);

        UnitCard bannerMedic = new UnitCard(CardNames.BANNER_MEDIC, CardPlace.FRIENDLY_SIEGE,
                5, false, new ArrayList<CardAbility>(){{
                    add(new UnitMedicAbility());
        }});
        allCards.add(bannerMedic);

        UnitCard blueStripesCommando1 = new UnitCard(CardNames.BLUE_STRIPS_COMMANDO, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.BLUE_COMMANDOS));
        }});
        allCards.add(blueStripesCommando1);

        UnitCard blueStripesCommando2 = new UnitCard(CardNames.BLUE_STRIPS_COMMANDO, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.BLUE_COMMANDOS));
        }});
        allCards.add(blueStripesCommando2);

        UnitCard blueStripesCommando3 = new UnitCard(CardNames.BLUE_STRIPS_COMMANDO, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.BLUE_COMMANDOS));
        }});
        allCards.add(blueStripesCommando3);

        UnitCard catapult1 = new UnitCard(CardNames.CATAPULT, CardPlace.FRIENDLY_SIEGE,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.CATAPULTS));
        }});
        allCards.add(catapult1);

        UnitCard catapult2 = new UnitCard(CardNames.CATAPULT, CardPlace.FRIENDLY_SIEGE,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.CATAPULTS));
        }});
        allCards.add(catapult2);

        UnitCard crinfirdDragonHunter1 = new UnitCard(CardNames.CRINFRID_DRAGON_HUNTER, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.DRAGON_HUNTERS));
        }});
        allCards.add(crinfirdDragonHunter1);

        UnitCard crinfirdDragonHunter2 = new UnitCard(CardNames.CRINFRID_DRAGON_HUNTER, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.DRAGON_HUNTERS));
        }});
        allCards.add(crinfirdDragonHunter2);

        UnitCard crinfirdDragonHunter3 = new UnitCard(CardNames.CRINFRID_DRAGON_HUNTER, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.DRAGON_HUNTERS));
        }});
        allCards.add(crinfirdDragonHunter3);

        UnitCard dethmold = new UnitCard(CardNames.DETHMOLD, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(dethmold);

        UnitCard dijkstra = new UnitCard(CardNames.DIJKSTRA, CardPlace.OPPONENT_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(dijkstra);

        UnitCard esterad = new UnitCard(CardNames.ESTERAD, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(esterad);

        UnitCard siegeExpert1 = new UnitCard(CardNames.SIEGE_EXPERT_1, CardPlace.FRIENDLY_SIEGE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
        }});
        allCards.add(siegeExpert1);

        UnitCard siegeExpert2 = new UnitCard(CardNames.SIEGE_EXPERT_2, CardPlace.FRIENDLY_SIEGE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
        }});
        allCards.add(siegeExpert2);

        UnitCard siegeExpert3 = new UnitCard(CardNames.SIEGE_EXPERT_3, CardPlace.FRIENDLY_SIEGE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
        }});
        allCards.add(siegeExpert3);

        UnitCard keira = new UnitCard(CardNames.KEIRA, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>());
        allCards.add(keira);

        UnitCard natalis = new UnitCard(CardNames.NATALIS, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(natalis);

        UnitCard philippa = new UnitCard(CardNames.PHILIPPA, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(philippa);

        UnitCard poorInfantry1 = new UnitCard(CardNames.POOR_INFANTRY, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.POOR_INFANTRY));
        }});
        allCards.add(poorInfantry1);

        UnitCard poorInfantry2 = new UnitCard(CardNames.POOR_INFANTRY, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.POOR_INFANTRY));
        }});
        allCards.add(poorInfantry2);

        UnitCard poorInfantry3 = new UnitCard(CardNames.POOR_INFANTRY, CardPlace.FRIENDLY_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.POOR_INFANTRY));
        }});
        allCards.add(poorInfantry3);

        UnitCard footSoldier1 = new UnitCard(CardNames.FOOT_SOLDIER_1, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>());
        allCards.add(footSoldier1);

        UnitCard footSoldier2 = new UnitCard(CardNames.FOOT_SOLDIER_2, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>());
        allCards.add(footSoldier2);

        UnitCard sabrina = new UnitCard(CardNames.SABRINA, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(sabrina);

        UnitCard sheale = new UnitCard(CardNames.SHEALA, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>());
        allCards.add(sheale);

        UnitCard sheldon = new UnitCard(CardNames.SHELDON, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(sheldon);

        UnitCard siegeTower = new UnitCard(CardNames.SIEGE_TOWER, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(siegeTower);

        UnitCard siegfried = new UnitCard(CardNames.SIEGFRIED, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(siegfried);

        UnitCard stennis = new UnitCard(CardNames.STENNIS, CardPlace.OPPONENT_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
                    add(new UnitSpyAbility());
        }});
        allCards.add(stennis);

        UnitCard thaler = new UnitCard(CardNames.THALER, CardPlace.OPPONENT_SIEGE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(thaler);

        UnitCard trebuchet1 = new UnitCard(CardNames.TREBUCHET_1, CardPlace.FRIENDLY_SIEGE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(trebuchet1);

        UnitCard trebuchet2 = new UnitCard(CardNames.TREBUCHET_2, CardPlace.FRIENDLY_SIEGE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(trebuchet2);

        UnitCard vernon = new UnitCard(CardNames.VERNON, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(vernon);

        UnitCard ves = new UnitCard(CardNames.VES, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(ves);

        UnitCard yarpen = new UnitCard(CardNames.YARPEN, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(yarpen);
    }

    private static void createScoiaTaelDeckCards() {
        LeaderCard francesca1 = new LeaderCard(CardNames.FRANCESCA_PUREBLOOD_ELF, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
                    add(new PureBloodElf());
        }});
        allCards.add(francesca1);

        LeaderCard francesca2 = new LeaderCard(CardNames.FRANCESCA_DAISY_OF_THE_VALLEY, CardPlace.FRIENDLY_LEADER,
                true, new ArrayList<CardAbility>(){{
            add(new DaisyOfTheValley());
        }});
        allCards.add(francesca2);

        LeaderCard francesca3 = new LeaderCard(CardNames.FRANCESCA_THE_BEAUTIFUL, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new TheBeautiful());
        }});
        allCards.add(francesca3);

        LeaderCard francesca4 = new LeaderCard(CardNames.FRANCESCA_HOPE_OF_AEN_SEIDHE, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new HopeOfTheAenSeidhe());
        }});
        allCards.add(francesca4);

        LeaderCard francesca5 = new LeaderCard(CardNames.FRANCESCA_QUEEN_OF_DOL_BLATHANNA, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new QueenOfDolBlathanna());
        }});
        allCards.add(francesca5);

        UnitCard barclay = new UnitCard(CardNames.BARCLAY, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
                    add(new UnitAgileAbility());
        }});
        allCards.add(barclay);

        UnitCard ciaran = new UnitCard(CardNames.CIARAN, CardPlace.FRIENDLY_CLOSE_RANGED,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(ciaran);

        UnitCard dennis = new UnitCard(CardNames.DENNIS, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(dennis);

        UnitCard dolArcher = new UnitCard(CardNames.DOL_ARCHER, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(dolArcher);

        UnitCard dolScout1 = new UnitCard(CardNames.DOL_SCOUT_1, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(dolScout1);

        UnitCard dolScout2 = new UnitCard(CardNames.DOL_SCOUT_2, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(dolScout2);

        UnitCard dolScout3 = new UnitCard(CardNames.DOL_SCOUT_3, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(dolScout3);

        UnitCard dwarvenSkirmisher1 = new UnitCard(CardNames.DWARVEN_SKIRMISHER_1, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.DWARVEN_SKIRMISHER));
        }});
        allCards.add(dwarvenSkirmisher1);

        UnitCard dwarvenSkirmisher2 = new UnitCard(CardNames.DWARVEN_SKIRMISHER_2, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.DWARVEN_SKIRMISHER));
        }});
        allCards.add(dwarvenSkirmisher2);

        UnitCard dwarvenSkirmisher3 = new UnitCard(CardNames.DWARVEN_SKIRMISHER_3, CardPlace.FRIENDLY_CLOSE,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.DWARVEN_SKIRMISHER));
        }});
        allCards.add(dwarvenSkirmisher3);

        UnitCard eithne = new UnitCard(CardNames.EITHNE, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(eithne);

        UnitCard elvenSkirmisher1 = new UnitCard(CardNames.ELVEN_SKIRMISHER_1, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ELVEN_SKIRMISHER));
        }});
        allCards.add(elvenSkirmisher1);

        UnitCard elvenSkirmisher2 = new UnitCard(CardNames.ELVEN_SKIRMISHER_2, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ELVEN_SKIRMISHER));
        }});
        allCards.add(elvenSkirmisher2);

        UnitCard elvenSkirmisher3 = new UnitCard(CardNames.ELVEN_SKIRMISHER_3, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.ELVEN_SKIRMISHER));
        }});
        allCards.add(elvenSkirmisher3);

        UnitCard filavandrel = new UnitCard(CardNames.FILAVANDREL, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(filavandrel);

        UnitCard havekarHealer1 = new UnitCard(CardNames.HAVEKAR_HEALER_1, CardPlace.FRIENDLY_RANGED,
                0, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(havekarHealer1);

        UnitCard havekarHealer2 = new UnitCard(CardNames.HAVEKAR_HEALER_2, CardPlace.FRIENDLY_RANGED,
                0, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(havekarHealer2);

        UnitCard havekarHealer3 = new UnitCard(CardNames.HAVEKAR_HEALER_3, CardPlace.FRIENDLY_RANGED,
                0, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(havekarHealer3);

        UnitCard havekarSmuggler1 = new UnitCard(CardNames.HAVEKAR_SMUGGLER_1, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.HAVEKAR_SMUGGLER));
        }});
        allCards.add(havekarSmuggler1);

        UnitCard havekarSmuggler2 = new UnitCard(CardNames.HAVEKAR_SMUGGLER_2, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.HAVEKAR_SMUGGLER));
        }});
        allCards.add(havekarSmuggler2);

        UnitCard havekarSmuggler3 = new UnitCard(CardNames.HAVEKAR_SMUGGLER_3, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.HAVEKAR_SMUGGLER));
        }});
        allCards.add(havekarSmuggler3);

        UnitCard ida = new UnitCard(CardNames.IDA, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(ida);

        UnitCard iorveth = new UnitCard(CardNames.IORVETH, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(iorveth);

        UnitCard isengrim = new UnitCard(CardNames.ISENGRIM, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>(){{
                    add(new UnitMoraleBoostAbility());
        }});
        allCards.add(isengrim);

        UnitCard mahakamenDefender1 = new UnitCard(CardNames.MAHAKAMEN_DEFENDER_1, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(mahakamenDefender1);

        UnitCard mahakamenDefender2 = new UnitCard(CardNames.MAHAKAMEN_DEFENDER_2, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(mahakamenDefender2);

        UnitCard mahakamenDefender3 = new UnitCard(CardNames.MAHAKAMEN_DEFENDER_3, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(mahakamenDefender3);

        UnitCard mahakamenDefender4 = new UnitCard(CardNames.MAHAKAMEN_DEFENDER_4, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(mahakamenDefender4);

        UnitCard mahakamenDefender5 = new UnitCard(CardNames.MAHAKAMEN_DEFENDER_5, CardPlace.FRIENDLY_CLOSE,
                5, false, new ArrayList<CardAbility>());
        allCards.add(mahakamenDefender5);

        UnitCard milva = new UnitCard(CardNames.MILVA, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
        }});
        allCards.add(milva);

        UnitCard riordain = new UnitCard(CardNames.RIORDAIN, CardPlace.FRIENDLY_RANGED,
                1, false, new ArrayList<CardAbility>());
        allCards.add(riordain);

        UnitCard saskia = new UnitCard(CardNames.SASKIA, CardPlace.FRIENDLY_RANGED,
                10, true, new ArrayList<CardAbility>());
        allCards.add(saskia);

        UnitCard schirru = new UnitCard(CardNames.SCHIRRU, CardPlace.FRIENDLY_SIEGE,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(schirru);

        UnitCard toruviel = new UnitCard(CardNames.TORUVIEL, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>());
        allCards.add(toruviel);

        UnitCard vriheddVeteran1 = new UnitCard(CardNames.VRIHEDD_VETERAN_1, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(vriheddVeteran1);

        UnitCard vriheddVeteran2 = new UnitCard(CardNames.VRIHEDD_VETERAN_2, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(vriheddVeteran2);

        UnitCard vriheddRecruit = new UnitCard(CardNames.VRIHEDD_RECRUIT, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(vriheddRecruit);

        UnitCard yaevinn = new UnitCard(CardNames.YAEVINN, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitAgileAbility());
        }});
        allCards.add(yaevinn);
    }

    private static void createSkelligeDeckCards() {
        LeaderCard crach = new LeaderCard(CardNames.CRACH_AN_CRAITE, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
                    add(new CrachAnCraite());
        }});
        allCards.add(crach);

        LeaderCard kingBran = new LeaderCard(CardNames.KING_BRAN, CardPlace.FRIENDLY_LEADER,
                true, new ArrayList<CardAbility>(){{
            add(new KingBran());
        }});
        allCards.add(kingBran);

        UnitCard berserkerMardroemed = new UnitCard(CardNames.BERSERKER_MARDOREMED, CardPlace.FRIENDLY_CLOSE,
                14, false, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
        }});

        UnitCard berserker = new UnitCard(CardNames.BERSERKER, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitBerserkerAbility(berserkerMardroemed));
        }});
        allCards.add(berserker);

        UnitCard birna = new UnitCard(CardNames.BIRNA_BRAN, CardPlace.FRIENDLY_CLOSE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(birna);

        UnitCard blueBoy = new UnitCard(CardNames.BLUE_BOY, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(blueBoy);

        UnitCard borkvarArcher = new UnitCard(CardNames.BROVKA_ARCHER, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(borkvarArcher);

        UnitCard cerys = new UnitCard(CardNames.CERYS, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.CERYS));
        }});
        allCards.add(cerys);

        UnitCard anCraiteWarrior1 = new UnitCard(CardNames.AN_CRAITE_WARRIOR, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.AN_CRAITE_WARRIORS));
        }});
        allCards.add(anCraiteWarrior1);

        UnitCard anCraiteWarrior2 = new UnitCard(CardNames.AN_CRAITE_WARRIOR, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.AN_CRAITE_WARRIORS));
        }});
        allCards.add(anCraiteWarrior2);

        UnitCard anCraiteWarrior3 = new UnitCard(CardNames.AN_CRAITE_WARRIOR, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.AN_CRAITE_WARRIORS));
        }});
        allCards.add(anCraiteWarrior3);

        UnitCard dimunPirate = new UnitCard(CardNames.DIMUN_PIRATE, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(dimunPirate);

        UnitCard donar = new UnitCard(CardNames.DONAR, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(donar);

        UnitCard draig = new UnitCard(CardNames.DRAIG, CardPlace.FRIENDLY_SIEGE,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitCommandersHornAbility());
        }});
        allCards.add(draig);

        UnitCard Ermion = new UnitCard(CardNames.ERMION, CardPlace.FRIENDLY_RANGED,
                8, true, new ArrayList<CardAbility>(){{
            add(new UnitMardroemeAbility());
        }});
        allCards.add(Ermion);

        UnitCard heymaeySkald = new UnitCard(CardNames.HEYMAEY_SKALD, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>());
        allCards.add(heymaeySkald);

        UnitCard hjalmar = new UnitCard(CardNames.HJALMAR, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(hjalmar);

        UnitCard holger = new UnitCard(CardNames.HOLGER, CardPlace.FRIENDLY_SIEGE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(holger);

        UnitCard kambiTransformed = new UnitCard(CardNames.KAMBI_TRANSFORMED, CardPlace.FRIENDLY_CLOSE,
                11, true, new ArrayList<CardAbility>());

        UnitCard kambi = new UnitCard(CardNames.KAMBI, CardPlace.FRIENDLY_CLOSE,
                0, false, new ArrayList<CardAbility>(){{
            add(new UnitTransformerAbility(kambiTransformed));
        }});
        allCards.add(kambi);

        UnitCard lightLongShip1 = new UnitCard(CardNames.LIGHT_LONG_SHIP, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.LIGHT_LONG_SHIP));
        }});
        allCards.add(lightLongShip1);

        UnitCard lightLongShip2 = new UnitCard(CardNames.LIGHT_LONG_SHIP, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.LIGHT_LONG_SHIP));
        }});
        allCards.add(lightLongShip2);

        UnitCard lightLongShip3 = new UnitCard(CardNames.LIGHT_LONG_SHIP, CardPlace.FRIENDLY_RANGED,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMusterAbility(CardMusterTypes.LIGHT_LONG_SHIP));
        }});
        allCards.add(lightLongShip3);

        UnitCard madmanLugos = new UnitCard(CardNames.MADMAN_LUGOS, CardPlace.FRIENDLY_CLOSE,
                6, false, new ArrayList<CardAbility>());
        allCards.add(madmanLugos);

        UnitCard olaf = new UnitCard(CardNames.OLAF, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>(){{
            add(new UnitMoraleBoostAbility());
            add(new UnitAgileAbility());
        }});
        allCards.add(olaf);

        UnitCard shieldMaiden1 = new UnitCard(CardNames.SHIELD_MAIDEN_1, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.SHIELD_MAIDENS));
        }});
        allCards.add(shieldMaiden1);

        UnitCard shieldMaiden2 = new UnitCard(CardNames.SHIELD_MAIDEN_2, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.SHIELD_MAIDENS));
        }});
        allCards.add(shieldMaiden2);

        UnitCard shieldMaiden3 = new UnitCard(CardNames.SHIELD_MAIDEN_3, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.SHIELD_MAIDENS));
        }});
        allCards.add(shieldMaiden3);

        UnitCard svanrige = new UnitCard(CardNames.SVANRIGE, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(svanrige);

        UnitCard tordarrochArmorSmith = new UnitCard(CardNames.TORDARROCH_ARMOR_SMITH, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(tordarrochArmorSmith);

        UnitCard udalryk = new UnitCard(CardNames.UDALRYK, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>());
        allCards.add(udalryk);

        UnitCard warLongShip1 = new UnitCard(CardNames.WAR_LONG_SHIP, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.WAR_LONG_SHIPS));
        }});
        allCards.add(warLongShip1);

        UnitCard warLongShip2 = new UnitCard(CardNames.WAR_LONG_SHIP, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.WAR_LONG_SHIPS));
        }});
        allCards.add(warLongShip2);

        UnitCard warLongShip3 = new UnitCard(CardNames.WAR_LONG_SHIP, CardPlace.FRIENDLY_SIEGE,
                6, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.WAR_LONG_SHIPS));
        }});
        allCards.add(warLongShip3);

        UnitCard youngBerserkerMardroemed1 = new UnitCard(CardNames.YOUNG_BERSERKER_MARDOREMED, CardPlace.FRIENDLY_RANGED,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.TRANSFORMED_YOUNG_BERSERKERS));
        }});

        UnitCard youngBerserkerMardroemed2 = new UnitCard(CardNames.YOUNG_BERSERKER_MARDOREMED, CardPlace.FRIENDLY_RANGED,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.TRANSFORMED_YOUNG_BERSERKERS));
        }});

        UnitCard youngBerserkerMardroemed3 = new UnitCard(CardNames.YOUNG_BERSERKER_MARDOREMED, CardPlace.FRIENDLY_RANGED,
                8, false, new ArrayList<CardAbility>(){{
            add(new UnitTightBondAbility(CardTightBTypes.TRANSFORMED_YOUNG_BERSERKERS));
        }});

        UnitCard youngBerserker1 = new UnitCard(CardNames.YOUNG_BERSERKER, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitBerserkerAbility(youngBerserkerMardroemed1));
        }});
        allCards.add(youngBerserker1);

        UnitCard youngBerserker2 = new UnitCard(CardNames.YOUNG_BERSERKER, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitBerserkerAbility(youngBerserkerMardroemed2));
        }});
        allCards.add(youngBerserker2);

        UnitCard youngBerserker3 = new UnitCard(CardNames.YOUNG_BERSERKER, CardPlace.FRIENDLY_RANGED,
                2, false, new ArrayList<CardAbility>(){{
            add(new UnitBerserkerAbility(youngBerserkerMardroemed3));
        }});
        allCards.add(youngBerserker3);
    }

    private static void createSevenKingdomsCards() {
        LeaderCard joffrey = new LeaderCard(CardNames.JOFFREY_BARATHEON, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new KingJoffrey());
        }});
        allCards.add(joffrey);

        LeaderCard robert = new LeaderCard(CardNames.ROBERT_BARATHEON, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new KingRobert());
        }});
        allCards.add(robert);

        LeaderCard cersei = new LeaderCard(CardNames.CERSEI_LANNISTER, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new QueenCersei());
        }});
        allCards.add(cersei);

        LeaderCard daenerys = new LeaderCard(CardNames.DAENERYS_TARGARYEN, CardPlace.FRIENDLY_LEADER,
                false, new ArrayList<CardAbility>(){{
            add(new QueenDaenerys());
        }});
        allCards.add(daenerys);

        UnitCard aryaStark = new UnitCard(CardNames.ARYA_STARK, CardPlace.FRIENDLY_CLOSE_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(aryaStark);

        UnitCard brienne = new UnitCard(CardNames.BRIENNE_OF_TARTH, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(brienne);

        UnitCard catelynStark = new UnitCard(CardNames.CATELYN_STARK, CardPlace.FRIENDLY_RANGED,
                5, false, new ArrayList<CardAbility>(){{
                    add(new UnitMedicAbility());
        }});
        allCards.add(catelynStark);

        UnitCard greyWorm = new UnitCard(CardNames.GREY_WORM, CardPlace.FRIENDLY_CLOSE,
                9, false, new ArrayList<CardAbility>());
        allCards.add(greyWorm);

        UnitCard jamieLannister = new UnitCard(CardNames.JAMIE_LANNISTER, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(jamieLannister);

        UnitCard jonSnow = new UnitCard(CardNames.JON_SNOW, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>(){{
                    add(new UnitMoraleBoostAbility());
        }});
        allCards.add(jonSnow);

        UnitCard ladyMelisandre = new UnitCard(CardNames.LADY_MELISANDRE, CardPlace.FRIENDLY_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(ladyMelisandre);

        UnitCard littleFinger = new UnitCard(CardNames.LITTLE_FINGER, CardPlace.OPPONENT_CLOSE,
                1, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(littleFinger);

        UnitCard lordVarys = new UnitCard(CardNames.LORD_VARYS, CardPlace.OPPONENT_SIEGE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(lordVarys);

        UnitCard margaeryTyrell = new UnitCard(CardNames.MARGAERY_TYRELL, CardPlace.FRIENDLY_RANGED,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitMedicAbility());
        }});
        allCards.add(margaeryTyrell);

        UnitCard nedStark = new UnitCard(CardNames.NED_STARK, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(nedStark);

        UnitCard robbStark = new UnitCard(CardNames.ROBB_STARK, CardPlace.FRIENDLY_CLOSE,
                8, true, new ArrayList<CardAbility>());
        allCards.add(robbStark);

        UnitCard sansaStark = new UnitCard(CardNames.SANSA_STARK, CardPlace.OPPONENT_RANGED,
                3, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(sansaStark);

        UnitCard serBronn = new UnitCard(CardNames.SER_BRONN, CardPlace.FRIENDLY_CLOSE,
                9, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(serBronn);

        UnitCard sirArthurDayne = new UnitCard(CardNames.SIR_ARTHUR_DAYNE, CardPlace.FRIENDLY_CLOSE,
                15, true, new ArrayList<CardAbility>());
        allCards.add(sirArthurDayne);

        UnitCard sirJorah = new UnitCard(CardNames.SIR_JORAH, CardPlace.FRIENDLY_CLOSE,
                10, true, new ArrayList<CardAbility>());
        allCards.add(sirJorah);

        UnitCard stannisBaratheon = new UnitCard(CardNames.STANNIS_BARATHEON, CardPlace.FRIENDLY_CLOSE,
                7, false, new ArrayList<CardAbility>(){{
                    add(new UnitMoraleBoostAbility());
        }});
        allCards.add(stannisBaratheon);

        UnitCard theHound = new UnitCard(CardNames.THE_HOUND, CardPlace.FRIENDLY_CLOSE,
                11, false, new ArrayList<CardAbility>(){{
            add(new UnitScorchAbility());
        }});
        allCards.add(theHound);

        UnitCard theMountain = new UnitCard(CardNames.THE_MOUNTAIN, CardPlace.FRIENDLY_CLOSE,
                11, true, new ArrayList<CardAbility>());
        allCards.add(theMountain);

        UnitCard tyrionLannister = new UnitCard(CardNames.TYRION_LANNISTER, CardPlace.OPPONENT_CLOSE,
                4, false, new ArrayList<CardAbility>(){{
            add(new UnitSpyAbility());
        }});
        allCards.add(tyrionLannister);

        UnitCard ygritte = new UnitCard(CardNames.YGRITTE, CardPlace.FRIENDLY_RANGED,
                6, false, new ArrayList<CardAbility>());
        allCards.add(ygritte);
    }
}
