package com.thisalv.checkpoketype.common;

import com.pixelmonmod.pixelmon.api.pokemon.Element;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thisalv.checkpoketype.common.utilities.PokemonMethods;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ICommandSource;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TypeCommand {
    private static final StringTextComponent ERR_DELIMITER =
            new StringTextComponent("§5-----------------------------------------------------");

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> hwCommand = Commands.literal("type")
                .then(Commands.argument("pokemon", MessageArgument.message())
                        .executes(TypeCommand::execute));

        dispatcher.register(hwCommand);
    }

    /*
     * Code from execute and checkType methods are from :
     * https://github.com/xPXpanD/EvenMorePixelmonCommands/blob/master/src/main/java/rs/expand/evenmorepixelmoncommands/commands/CheckTypes.java
     */

    private static int execute(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ICommandSource src;
        final Entity entitySrc = ctx.getSource().getEntity();
        final PokemonMethods enumData;
        boolean inputIsInt = false;
        final String[] args;
        String arg1String;
        final String arg2String;
        final int inputInteger;

        if (entitySrc != null)
            src = entitySrc.getEntity();
        else
            src = ctx.getSource().getServer();

        args = MessageArgument.getMessage(ctx, "pokemon").getString().split("\\s");

        if (args.length == 0) {
            printLocalError(src, "Must provide pokémon name or ID!");
            return 0;
        }

        arg1String = args[0];

        if (arg1String.matches("-?\\d+"))
        {
            inputIsInt = true;
            inputInteger = Integer.parseInt(arg1String);

            // TODO: Update if new Pokémon are added!
            if (inputInteger > 893 || inputInteger < 1)
            {
                // TODO: Update if new Pokémon are added!
                printLocalError(src, "Invalid Pokédex number! Valid range is 1-893.");
                return 0;
            }
            else
                enumData = PokemonMethods.getPokemonFromID(inputInteger);
        }
        else
        {
            switch (arg1String.toUpperCase())
            {
                // Fix dodgy inputs and names that are different internally for technical reasons.
                case "NIDORANF": case "FNIDORAN": case "FEMALENIDORAN": case "NIDORAN♀":
                arg1String = "NidoranFemale"; break;
                case "NIDORANM": case "MNIDORAN": case "MALENIDORAN": case "NIDORAN♂":
                arg1String = "NidoranMale"; break;
                case "FARFETCH'D": case "FARFETCHED":
                arg1String = "Farfetchd"; break;
                case "MR.MIME": case "MISTERMIME":
                arg1String = "MrMime"; break;
                case "HO-OH":
                    arg1String = "HoOh"; break;
                case "MIMEJR.": case "MIMEJUNIOR":
                arg1String = "MimeJr"; break;
                case "FLABÉBÉ": case "FLABÈBÈ":
                arg1String = "Flabebe"; break;
                case "TYPE:NULL":
                    arg1String = "TypeNull"; break;
                case "JANGMO-O":
                    arg1String = "JangmoO"; break;
                case "HAKAMO-O":
                    arg1String = "HakamoO"; break;
                case "KOMMO-O":
                    arg1String = "KommoO"; break;
                case "SIRFETCH'D": case "SIRFETCHED":
                arg1String = "Sirfetchd"; break;
                case "MR.RIME": case "MISTERRIME":
                arg1String = "MrRime"; break;
            }

            if (args.length > 1)
            {
                // Get the contents of the second argument, if provided. Validate.
                arg2String = args[1];
                switch (arg2String.toUpperCase())
                {
                    // Alolan variants. (and Meowth, two regional variants so we handle it as a special case)
                    case "RATTATA": case "RATICATE": case "RAICHU": case "SANDSHREW": case "SANDSLASH": case "VULPIX":
                    case "MEOWTH": case "NINETALES": case "DIGLETT": case "DUGTRIO": case "PERSIAN": case "GEODUDE":
                    case "GRAVELER": case "GOLEM": case "GRIMER": case "MUK": case "EXEGGUTOR": case "MAROWAK":
                {
                    if (arg1String.equalsIgnoreCase("Alolan"))
                        arg1String = arg2String + arg1String;
                    else if (arg1String.equalsIgnoreCase("Galarian") && arg2String.equalsIgnoreCase("MEOWTH"))
                        arg1String = arg2String + arg1String;

                    break;
                }

                // Galarian variants.
                    case "PONYTA": case "RAPIDASH": case "SLOWPOKE": case "SLOWBRO": case "FARFETCHD":
                    case "WEEZING": case "MRMIME": case "ARTICUNO": case "ZAPDOS": case "MOLTRES": case "CORSOLA":
                    case "ZIGZAGOON": case "LINOONE": case "DARUMAKA": case "DARMANITAN": case "YAMASK": case "STUNFISK":
                {
                    if (arg1String.equalsIgnoreCase("Galarian"))
                        arg1String = arg2String + arg1String;

                    break;
                }

                // Generic (potential) two-word names.
                    case "OH":
                    {
                        if (arg1String.toUpperCase().equals("HO"))
                            arg1String = "HoOh";

                        break;
                    }
                    case "O":
                    {
                        switch (arg1String.toUpperCase())
                        {
                            case "JANGMO":
                                arg1String = "JangmoO"; break;
                            case "HAKAMO":
                                arg1String = "HakamoO"; break;
                            case "KOMMO":
                                arg1String = "KommoO"; break;
                        }

                        break;
                    }
                    case "NULL":
                    {
                        if (arg1String.toUpperCase().equals("TYPE") || arg1String.toUpperCase().equals("TYPE:"))
                            arg1String = "TypeNull";

                        break;
                    }
                    case "KOKO":
                    {
                        if (arg1String.toUpperCase().equals("TAPU"))
                            arg1String = "TapuKoko";

                        break;
                    }
                    case "LELE":
                    {
                        if (arg1String.toUpperCase().equals("TAPU"))
                            arg1String = "TapuLele";

                        break;
                    }
                    case "BULU":
                    {
                        if (arg1String.toUpperCase().equals("TAPU"))
                            arg1String = "TapuBulu";

                        break;
                    }
                    case "FINI":
                    {
                        if (arg1String.toUpperCase().equals("TAPU"))
                            arg1String = "TapuFini";

                        break;
                    }
                }
            }

            enumData = PokemonMethods.getPokemonFromName(arg1String);

            if (enumData == null)
            {
                printLocalError(src, "Invalid Pokémon! Check your spelling, or try a number.");
                return 0;
            }
        }

        // Let's do this thing!
        checkTypes(enumData, inputIsInt, src);

        return 1;
    }

    private static void checkTypes(final PokemonMethods enumData, final boolean inputIsInt, final ICommandSource src) {
        // Check for differently typed forms or regional variants. Use fallthroughs to flag specific dex IDs as special.
        boolean hasForms = false, hasRegionalVariants = false;
        if (inputIsInt)
        {
            switch (enumData.index)
            {
                // Differently-typed forms.
                case 351: case 383: case 413: case 479: case 492: case 493: case 648: case 720: case 741: case 773:
                case 800: case 888: case 889: case 892:
            {
                hasForms = true;
                break;
            }

            // Alolan and Galarian variants.
                case 19: case 20: case 26: case 27: case 28: case 37: case 38: case 50: case 51: case 52: case 53:
                case 74: case 75: case 76: case 88: case 89: case 103: case 105:
                case 77: case 78: case 79: case 80: case 83: case 110: case 122: case 144: case 145: case 146: case 222:
                case 263: case 264: case 554: case 562: case 618:
            {
                hasRegionalVariants = true;
                break;
            }

            // Darmanitan. Has a Galarian form with different types. Yeah, I know.
                case 555:
                {
                    hasForms = true;
                    hasRegionalVariants = true;
                    break;
                }
            }
        }
        else
        {
            switch (enumData.name().toUpperCase())
            {
                // Differently-typed forms.
                case "CASTFORM": case "GROUDON": case "WORMADAM": case "ROTOM": case "SHAYMIN": case "ARCEUS":
                case "MELOETTA": case "HOOPA": case "ORICORIO": case "SILVALLY": case "NECROZMA": case "ZACIAN":
                case "ZAMAZENTA": case "URSHIFU":
            {
                hasForms = true;
                break;
            }

            // Alolan and Galarian variants.
                case "RATTATA": case "RATICATE": case "RAICHU": case "SANDSHREW": case "SANDSLASH": case "VULPIX":
                case "MEOWTH": case "NINETALES": case "DIGLETT": case "DUGTRIO": case "PERSIAN": case "GEODUDE":
                case "GRAVELER": case "GOLEM": case "GRIMER": case "MUK": case "EXEGGUTOR": case "MAROWAK":
                case "PONYTA": case "RAPIDASH": case "SLOWPOKE": case "SLOWBRO": case "FARFETCHD": case "WEEZING":
                case "MRMIME": case "ARTICUNO": case "ZAPDOS": case "MOLTRES": case "CORSOLA": case "ZIGZAGOON":
                case "LINOONE": case "DARUMAKA": case "YAMASK": case "STUNFISK":
            {
                hasRegionalVariants = true;
                break;
            }

            // Darmanitan. Has a Galarian form with different types. Yeah, I know.
                case "DARMANITAN":
                {
                    hasForms = true;
                    hasRegionalVariants = true;
                    break;
                }
            }
        }

        // Set up internal variables for (almost) EVERYTHING. Plenty of room to improve, but it'll work for now.
        boolean type2Present = true;
        final char arrowChar = '➡';
        final String typeMessage;
        if (enumData.type2 == null)
            type2Present = false;

        final String typeString =
                "§fNormal, §4Fighting, §9Flying, §5Poison, §6Ground, " +
                        "§7Rock, §2Bug, §5Ghost, §7Steel, §cFire, §3Water, " +
                        "§aGrass, §eElectric, §dPsychic, §bIce, §9Dragon, " +
                        "§8Dark, §dFairy";
        final String[] typeList = typeString.split(", ");

        final String unformattedTypeString =
                "Normal, Fighting, Flying, Poison, Ground, Rock, Bug, Ghost, Steel, " +
                        "Fire, Water, Grass, Electric, Psychic, Ice, Dragon, Dark, Fairy";
        final String[] unformattedTypeList = unformattedTypeString.split(", ");

        final List<Element> foundTypes = new ArrayList<>();
        foundTypes.add(Element.parseType(enumData.type1));
        final int indexType1 = Arrays.asList(unformattedTypeList).indexOf(enumData.type1);
        final int indexType2;
        if (type2Present)
        {
            foundTypes.add(Element.parseType(enumData.type2));
            indexType2 = Arrays.asList(unformattedTypeList).indexOf(enumData.type2);

            // Used way later, but setting it up now avoids some repeated code.
            typeMessage = " §f(" + typeList[indexType1] + "§f, " + typeList[indexType2] + "§f)";
        }
        else
            typeMessage = " §f(" + typeList[indexType1] + "§f)";

        // Run through the big list of Pokémon and check the target's type(s).
        final StringBuilder weaknessBuilder2x = new StringBuilder();
        final StringBuilder weaknessBuilder4x = new StringBuilder();
        final StringBuilder strengthBuilder50p = new StringBuilder();
        final StringBuilder strengthBuilder25p = new StringBuilder();
        final StringBuilder immunityBuilder = new StringBuilder();

        // Run through types and prep for printing strenghts/weaknesses later.
        for (int i = 1; i < 19; i++)
        {
            final Element typeToTest = Element.parseType(unformattedTypeList[i - 1]);
            final float typeEffectiveness = Element.getTotalEffectiveness(foundTypes, typeToTest);

            if (typeEffectiveness < 1.0f)
            {
                if (typeEffectiveness == 0.5f) // 50% effectiveness
                    strengthBuilder50p.append(typeList[i - 1]).append("§f, ");
                else if (typeEffectiveness == 0.25f) // 25% effectiveness
                    strengthBuilder25p.append(typeList[i - 1]).append("§f, ");
                else if (typeEffectiveness == 0.00f) // Immune!
                    immunityBuilder.append(typeList[i - 1]).append("§f, ");
            }
            else if (typeEffectiveness > 1.0f)
            {
                if (typeEffectiveness == 2.0f) // 200% effectiveness
                    weaknessBuilder2x.append(typeList[i - 1]).append("§f, ");
                else if (typeEffectiveness == 4.0f) // 400% effectiveness, ouch!
                    weaknessBuilder4x.append(typeList[i - 1]).append("§f, ");
            }
        }

        // Get a formatted title that shows the Pokémon's ID, name and, if applicable, form name. Print.
        src.sendMessage(new StringTextComponent("§7-----------------------------------------------------"), Util.NIL_UUID);
        src.sendMessage(new StringTextComponent(PokemonMethods.getTitleWithIDAndFormName(enumData.index, enumData.name()) + typeMessage), Util.NIL_UUID);
        src.sendMessage(new StringTextComponent(""), Util.NIL_UUID);

        // Get resistances, weaknesses and immunities. Print to chat.
        if (weaknessBuilder2x.length() != 0 || weaknessBuilder4x.length() != 0)
        {
            src.sendMessage(new StringTextComponent("§cWeaknesses§f:"), Util.NIL_UUID);
            if (weaknessBuilder4x.length() != 0)
            {
                weaknessBuilder4x.setLength(weaknessBuilder4x.length() - 2); // Cut off the last comma.
                src.sendMessage(new StringTextComponent(arrowChar + " §c400%§f: " + weaknessBuilder4x), Util.NIL_UUID);
            }
            if (weaknessBuilder2x.length() != 0)
            {
                weaknessBuilder2x.setLength(weaknessBuilder2x.length() - 2); // Cut off the last comma.
                src.sendMessage(new StringTextComponent(arrowChar + " §c200%§f: " + weaknessBuilder2x), Util.NIL_UUID);
            }
        }

        if (strengthBuilder50p.length() != 0 || strengthBuilder25p.length() != 0)
        {
            src.sendMessage(new StringTextComponent("§aResistances§f:"), Util.NIL_UUID);
            if (strengthBuilder50p.length() != 0)
            {
                strengthBuilder50p.setLength(strengthBuilder50p.length() - 2); // Cut off the last comma.
                src.sendMessage(new StringTextComponent(arrowChar + " §a50%§f: " + strengthBuilder50p), Util.NIL_UUID);
            }
            if (strengthBuilder25p.length() != 0)
            {
                strengthBuilder25p.setLength(strengthBuilder25p.length() - 2); // Cut off the last comma.
                src.sendMessage(new StringTextComponent(arrowChar + " §a25%§f: " + strengthBuilder25p), Util.NIL_UUID);
            }
        }

        // Find and format a Pokémon's type-relevant abilities.
        src.sendMessage(new StringTextComponent("§bImmunities§f:"), Util.NIL_UUID);

        // Abilities/hovers are linked. If one has two entries, the other will have two, too!
        final List<String> abilities = new ArrayList<>();
        final List<String> hovers = new ArrayList<>();

        if (immunityBuilder.length() == 0)
            immunityBuilder.append("§8None"); // Shown when a Pokémon isn't immune against anything.
        else
            immunityBuilder.setLength(immunityBuilder.length() - 2); // Shank any trailing commas.

        StringTextComponent immunityStart = new StringTextComponent(arrowChar + " §b0%§f: " + immunityBuilder + "§7 (may have ");

        // Make a bunch of lists for different type-nullifying abilities.
        // TODO: Update this for gen 7/8!
        final String bigPecks =
                "Pidove, Tranquill, Unfezant, Ducklett, Swanna, Vullaby, Mandibuzz, Fletchling, Pidgey, Pidgeotto, " +
                        "Pidgeot, Chatot";
        final String clearBody =
                "Tentacool, Tentacruel, Beldum, Metang, Metagross, Regirock, Regice, Registeel, Carbink, Diancie, " +
                        "Klink, Klang, Klinklang";
        final String damp =
                "Psyduck, Golduck, Paras, Parasect, Horsea, Seadra, Kingdra, Mudkip, Marshtomp, Swampert, Frillish, " +
                        "Jellicent, Poliwag, Poliwhirl, Poliwrath, Politoed, Wooper, Quagsire";
        final String drySkin =
                "Paras, Parasect, Croagunk, Toxicroak, Helioptile, Heliolisk, Jynx";
        final String flashFire =
                "Vulpix, Ninetales, Growlithe, Arcanine, Ponyta, Rapidash, Flareon, Houndour, Houndoom, Heatran, " +
                        "Litwick, Lampent, Chandelure, Heatmor, Cyndaquil, Quilava, Typhlosion, Entei";
        final String hyperCutter =
                "Krabby, Kingler, Pinsir, Gligar, Mawile, Trapinch, Corphish, Crawdaunt, Gliscor, Crabrawler, " +
                        "Crabominable";
        final String justified =
                "Growlithe, Arcanine, Absol, Lucario, Gallade, Cobalion, Terrakion, Virizion, Keldeo";
        final String levitate =
                "Gastly, Haunter, Gengar, Koffing, Weezing, Misdreavus, Unown, Vibrava, Flygon, Lunatone, Solrock, " +
                        "Baltoy, Claydol, Duskull, Chimecho, Latias, Latios, Mismagius, Chingling, Bronzor, Bronzong, " +
                        "Carnivine, Rotom, RotomHeat, RotomWash, RotomFrost, RotomFan, RotomMow, Uxie, Mesprit, Azelf, " +
                        "Giratina, Cresselia, Tynamo, Eelektrik, Eelektross, Cryogonal, Hydreigon, Vikavolt";
        final String lightningRod =
                "Cubone, Marowak, Rhyhorn, Rhydon, Electrike, Manectric, Rhyperior, Blitzle, Zebstrika, Pikachu, " +
                        "Raichu, Goldeen, Seaking, Zapdos, Pichu, Plusle, Sceptile, MarowakAlolan";
        final String motorDrive =
                "Electivire, Blitzle, Zebstrika, Emolga";
        final String sapSipper =
                "Deerling, Sawsbuck, Bouffalant, Skiddo, Gogoat, Goomy, Sliggoo, Goodra, Drampa, Marill, Azumarill, " +
                        "Girafarig, Stantler, Miltank, Azurill, Blitzle, Zebstrika";
        final String soundProof =
                "Voltorb, Electrode, MrMime, Whismur, Loudred, Exploud, MimeJr, Shieldon, Bastiodon, Snover, " +
                        "Abomasnow, Bouffalant";
        final String stormDrain =
                "Lileep, Cradily, Shellos, Gastrodon, Finneon, Lumineon, Maractus";
        final String sturdy =
                "Geodude, Graveler, Golem, Magnemite, Magneton, Onix, Sudowoodo, Pineco, Forretress, Steelix, " +
                        "Shuckle, Skarmory, Donphan, Nosepass, Aron, Lairon, Aggron, Shieldon, Bastiodon, Bonsly, Magnezone, " +
                        "Probopass, Roggenrola, Boldore, Gigalith, Sawk, Dwebble, Crustle, Tirtouga, Carracosta, Relicanth, " +
                        "Regirock, Tyrunt, Carbink, Bergmite, Avalugg, GeodudeAlolan, GravelerAlolan, GolemAlolan";
        final String suctionCups =
                "Octillery, Lileep, Cradily, Inkay, Malamar";
        final String voltAbsorb =
                "Jolteon, Chinchou, Lanturn, Thundurus, Raikou, Minun, Pachirisu, Zeraora";
        final String waterAbsorb =
                "Lapras, Vaporeon, Mantine, Mantyke, Maractus, Volcanion, Chinchou, Lanturn, Suicune, Cacnea, " +
                        "Cacturne, Tympole, Palpitoad, Seismitoad, Frillish, Jellicent, Poliwag, Poliwhirl, Poliwrath, " +
                        "Politoed, Wooper, Quagsire";

        // Check if Pokémon are on these lists. Create nice Strings to print to chat and add as hovers.
        if (motorDrive.contains(enumData.name()))
        {
            abilities.add("§f§l§nMotor Drive");
            hovers.add("§7§lMotor Drive §r§7nullifies §eElectric §7damage.");
        }
        if (suctionCups.contains(enumData.name()))
        {
            abilities.add("§f§l§nSuction Cups");
            hovers.add("§7§lSuction Cups §r§7prevents §nswitch-out§r§7 moves.");
        }
        if (voltAbsorb.contains(enumData.name()))
        {
            abilities.add("§f§l§nVolt Absorb");
            hovers.add("§7§lVolt Absorb §r§7nullifies §eElectric §7damage.");
        }
        if (stormDrain.contains(enumData.name()))
        {
            abilities.add("§f§l§nStorm Drain");
            hovers.add("§7§lStorm Drain §r§7nullifies §3Water §7damage.");
        }
        if (drySkin.contains(enumData.name()))
        {
            abilities.add("§f§l§nDry Skin");
            hovers.add("§7§lDry Skin §r§7adds 25% §3Water §7absorbance but §cFire §7hurts 25% more.");
        }
        if (justified.contains(enumData.name()))
        {
            abilities.add("§f§l§nJustified");
            hovers.add("§7§lJustified §r§7ups §nAttack§r§7 by one stage when hit by a §8Dark §7move.");
        }
        if (hyperCutter.contains(enumData.name()))
        {
            abilities.add("§f§l§nHyper Cutter");
            hovers.add("§7§lHyper Cutter §r§7prevents a Pokémon's Attack from being dropped.");
        }
        if (soundProof.contains(enumData.name()))
        {
            abilities.add("§f§l§nSoundproof");
            hovers.add("§7§lSoundproof §r§7nullifies §nsound-based§r§7 moves.");
        }
        if (bigPecks.contains(enumData.name()))
        {
            abilities.add("§f§l§nBig Pecks");
            hovers.add("§7§lBig Pecks §r§7prevents a Pokémon's Defense from being dropped.");
        }
        if (clearBody.contains(enumData.name()))
        {
            abilities.add("§f§l§nClear Body");
            hovers.add("§7§lClear Body §r§7prevents all of a Pokémon's stats from being dropped.");
        }
        if (sapSipper.contains(enumData.name()))
        {
            abilities.add("§f§l§nSap Sipper");
            hovers.add("§7§lSap Sipper §r§7nullifies §aGrass §7damage.");
        }
        if (damp.contains(enumData.name()))
        {
            abilities.add("§f§l§nDamp");
            hovers.add("§7§lDamp §r§7disables §nSelf-Destruct§r§7/§nExplosion§r§7.");
        }
        if (lightningRod.contains(enumData.name()))
        {
            abilities.add("§f§l§nLightning Rod");
            hovers.add("§7§lLightning Rod §r§7nullifies §eElectric §7damage.");
        }
        if (flashFire.contains(enumData.name()))
        {
            abilities.add("§f§l§nFlash Fire");
            hovers.add("§7§lFlash Fire §r§7nullifies §cFire §7damage.");
        }
        if (waterAbsorb.contains(enumData.name()))
        {
            abilities.add("§f§l§nWater Absorb");
            hovers.add("§7§lWater Absorb §r§7nullifies §3Water §7damage.");
        }
        if (sturdy.contains(enumData.name()))
        {
            abilities.add("§f§l§nSturdy");
            hovers.add("§7§lSturdy §r§7prevents §n1-hit KO§r§7 attacks.");
        }
        if (levitate.contains(enumData.name()))
        {
            abilities.add("§f§l§nLevitate");
            hovers.add("§7§lLevitate §r§7nullifies §eGround §7damage.");
        }

        // Check if we have certain unique Pokémon with unique abilities.
        if (enumData.name().equals("Torkoal") || enumData.name().equals("Heatmor"))
        {
            abilities.add("§f§l§nWhite Smoke");
            hovers.add("§7§lWhite Smoke §7provides immunity to stat reduction.");
        }
        else if (enumData.name().contains("Shedinja"))
        {
            abilities.add("§f§l§nWonder Guard");
            hovers.add("§7§lWonder Guard §7disables all §nnon-super effective§r§7 damage.");
            immunityStart = new StringTextComponent(arrowChar + " §b0%§f: " + immunityBuilder + "§7 (has "); // Less awkward.
        }

        // Figure out what to show in chat, and how to show it.
        final StringTextComponent immunityPair;
        final StringTextComponent immunityPair2;
        final StringTextComponent immunityPair3;
        final String immunityEnd = "§r§7)";
        switch (abilities.size())
        {
            case 1:
            {
                immunityPair = new StringTextComponent(abilities.get(0));

                src.sendMessage(immunityStart.append(immunityPair).append(immunityEnd), Util.NIL_UUID);
                break;
            }
            case 2:
            {
                final StringTextComponent orMessage = new StringTextComponent("§r§7 or §f§l§n");
                immunityPair = new StringTextComponent(abilities.get(0));
                immunityPair2 = new StringTextComponent(abilities.get(1));

                src.sendMessage(immunityStart.append(immunityPair).append(orMessage).append(immunityPair2).append(immunityEnd), Util.NIL_UUID);
                break;
            }
            case 3:
            {
                // Overwrite this here so we can squeeze in more info.
                // Not ideal, but not rolling over to double lines is nice.
                immunityStart = new StringTextComponent(
                        arrowChar + " §b0%§f: " + immunityBuilder + "§7 (may have type abilities, see below)");

                final StringTextComponent orMessage = new StringTextComponent("§r§7 or §f§l§n");
                final StringTextComponent newLineFormat = new StringTextComponent(arrowChar + " §b=>§f: ");
                immunityPair = new StringTextComponent(abilities.get(0));
                immunityPair2 = new StringTextComponent(abilities.get(1));
                immunityPair3 = new StringTextComponent(abilities.get(2));

                src.sendMessage(immunityStart, Util.NIL_UUID);
                src.sendMessage(newLineFormat.append(immunityStart).append(immunityPair).append(orMessage).append(immunityPair2)
                        .append(orMessage).append(immunityPair3).append(immunityEnd), Util.NIL_UUID);
                break;
            }
            default:
                src.sendMessage(new StringTextComponent(arrowChar + " §b0%§f: " + immunityBuilder), Util.NIL_UUID);
        }

        // Print messages if differently typed forms or Alolan forms are available.
        if (hasForms)
        {
            src.sendMessage(new StringTextComponent(""), Util.NIL_UUID);

            final String formHelper = "§cForms found! §6/type ";
            switch (enumData.name())
            {
                // Big ones. Provide just the names to keep stuff manageable. Some of these are super squished by necessity.
                case "Castform":
                    src.sendMessage(new StringTextComponent(formHelper + "CastformSunny §f(or §6Rainy§f/§6Snowy§f)"), Util.NIL_UUID); break;
                case "Wormadam":
                    src.sendMessage(new StringTextComponent(formHelper + "WormadamSandy§f, §6WormadamTrash"), Util.NIL_UUID); break;
                case "Rotom":
                    src.sendMessage(new StringTextComponent(formHelper + "RotomHeat §f(or §6Wash§f/§6Frost§f/§6Fan§f/§6Mow§f)"), Util.NIL_UUID); break;
                case "Arceus":
                    src.sendMessage(new StringTextComponent(formHelper + "ArceusTYPE §f(where \"§6TYPE§f\" is a type)"), Util.NIL_UUID); break;
                case "Darmanitan":
                    src.sendMessage(new StringTextComponent(formHelper + "DarmanitanZen §f(or §6DarmanitanZenGalarian§f)"), Util.NIL_UUID); break;
                case "Silvally":
                    src.sendMessage(new StringTextComponent(formHelper + "SilvallyTYPE §f(where \"§6TYPE§f\" is a type)"), Util.NIL_UUID); break;
                case "Necrozma":
                    src.sendMessage(new StringTextComponent(formHelper + "NecrozmaDuskMane §f(or §6DawnWings§f/§6Ultra§f)"), Util.NIL_UUID); break;
                case "Oricorio": // This one hurts.
                {
                    src.sendMessage(new StringTextComponent("§cShowing Baile, check: §6/type" +
                            " OricorioPomPom §f(or §6Pau§f/§6Sensu§f)"), Util.NIL_UUID);

                    break;
                }

                // Small ones. We can show types on these, like the Alolan variants.
                case "Groudon":
                    src.sendMessage(new StringTextComponent(formHelper + "GroudonPrimal §f(§6Ground§f, §cFire§f)"), Util.NIL_UUID); break;
                case "Shaymin":
                    src.sendMessage(new StringTextComponent(formHelper + "ShayminSky §f(§aGrass§f, §9Flying§f)"), Util.NIL_UUID); break;
                case "Meloetta":
                    src.sendMessage(new StringTextComponent(formHelper + "MeloettaPirouette §f(Normal, §4Fighting§f)"), Util.NIL_UUID); break;
                case "Hoopa":
                    src.sendMessage(new StringTextComponent(formHelper + "HoopaUnbound §f(§dPsychic§f, §8Dark§f)"), Util.NIL_UUID); break;
                case "Zacian":
                    src.sendMessage(new StringTextComponent(formHelper + "ZacianCrowned §f(§dFairy§f, §7Steel§f)"), Util.NIL_UUID); break;
                case "Zamazenta":
                    src.sendMessage(new StringTextComponent(formHelper + "ZamazentaCrowned §f(§4Fighting§f, §7Steel§f)"), Util.NIL_UUID); break;
                case "Urshifu":
                    src.sendMessage(new StringTextComponent(formHelper + "UrshifuRapid §f(§4Fighting§f, §3Water§f)"), Util.NIL_UUID); break;
            }
        }
        else if (hasRegionalVariants)
        {
            final String formHelper;
            src.sendMessage(new StringTextComponent(""), Util.NIL_UUID);

            // Special check for Meowth since it has two regional forms.
            if (enumData.name().equals("Meowth"))
            {
                formHelper = "§cRegional forms found! §6/type ";
                src.sendMessage(new StringTextComponent(formHelper + "Alolan or Galarian Meowth"), Util.NIL_UUID);
            }
            else
            {
                formHelper = "§cRegional form found! §6/type ";
                switch (enumData.name())
                {
                    // Alolan variants.
                    case "Rattata":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Rattata §f(§8Dark§f, Normal)"), Util.NIL_UUID); break;
                    case "Raticate":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Raticate §f(§8Dark§f, Normal)"), Util.NIL_UUID); break;
                    case "Raichu":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Raichu §f(§eElectric§f, §dPsychic§f)"), Util.NIL_UUID); break;
                    case "Sandshrew":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Sandshrew §f(§bIce§f, §7Steel§f)"), Util.NIL_UUID); break;
                    case "Sandslash":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Sandslash §f(§bIce§f, §7Steel§f)"), Util.NIL_UUID); break;
                    case "Vulpix":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Vulpix §f(§bIce§f)"), Util.NIL_UUID); break;
                    case "Ninetales":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Ninetales §f(§bIce§f, §dFairy§f)"), Util.NIL_UUID); break;
                    case "Diglett":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Diglett §f(§6Ground§f, §7Steel§f)"), Util.NIL_UUID); break;
                    case "Dugtrio":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Dugtrio §f(§6Ground§f, §7Steel§f)"), Util.NIL_UUID); break;
                    case "Persian":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Persian §f(§8Dark§f)"), Util.NIL_UUID); break;
                    case "Geodude":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Geodude §f(§7Rock§f, §eElectric§f)"), Util.NIL_UUID); break;
                    case "Graveler":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Graveler §f(§7Rock§f, §eElectric§f)"), Util.NIL_UUID); break;
                    case "Golem":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Golem §f(§7Rock§f, §eElectric§f)"), Util.NIL_UUID); break;
                    case "Grimer":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Grimer §f(§5Poison§f, §8Dark§f)"), Util.NIL_UUID); break;
                    case "Muk":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Muk §f(§5Poison§f, §8Dark§f)"), Util.NIL_UUID); break;
                    case "Exeggutor":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Exeggutor §f(§aGrass§f, §9Dragon§f)"), Util.NIL_UUID); break;
                    case "Marowak":
                        src.sendMessage(new StringTextComponent(formHelper + "Alolan Marowak §f(§cFire§f, §5Ghost§f)"), Util.NIL_UUID); break;

                    // Galarian variants.
                    case "Ponyta":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Ponyta §f(§dPsychic§f)"), Util.NIL_UUID); break;
                    case "Rapidash":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Rapidash §f(§dPsychic§f, §dFairy§f)"), Util.NIL_UUID); break;
                    case "Slowpoke":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Slowpoke §f(§dPsychic§f)"), Util.NIL_UUID); break;
                    case "Slowbro":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Slowbro §f(§5Poison§f, §dPsychic§f)"), Util.NIL_UUID); break;
                    case "Farfetchd":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Farfetch'd §f(§4Fighting§f)"), Util.NIL_UUID); break;
                    case "Weezing":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Weezing §f(§5Poison§f, §dFairy§f)"), Util.NIL_UUID); break;
                    case "MrMime":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Mr. Mime §f(§bIce§f, §dPsychic§f)"), Util.NIL_UUID); break;
                    case "Articuno":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Articuno §f(§dPsychic§f, §9Flying§f)"), Util.NIL_UUID); break;
                    case "Zapdos":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Zapdos §f(§4Fighting§f, §9Flying§f)"), Util.NIL_UUID); break;
                    case "Moltres":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Zapdos §f(§8Dark§f, §9Flying§f)"), Util.NIL_UUID); break;
                    case "Corsola":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Corsola §f(§5Ghost§f)"), Util.NIL_UUID); break;
                    case "Zigzagoon":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Zigzagoon §f(§8Dark§f, Normal)"), Util.NIL_UUID); break;
                    case "Linoone":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Linoone §f(§8Dark§f, Normal)"), Util.NIL_UUID); break;
                    case "Darumaka":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Darumaka §f(§bIce§f)"), Util.NIL_UUID); break;
                    case "Yamask":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Yamask §f(§6Ground§f, §5Ghost§f)"), Util.NIL_UUID); break;
                    case "Stunfisk":
                        src.sendMessage(new StringTextComponent(formHelper + "Galarian Stunfisk §f(§6Ground§f, §7Steel§f)"), Util.NIL_UUID); break;
                }
            }
        }

        src.sendMessage(new StringTextComponent("§7-----------------------------------------------------"), Util.NIL_UUID);
    }

    private static void printLocalError(final ICommandSource src, final String message) {
        src.sendMessage(ERR_DELIMITER, Util.NIL_UUID);
        src.sendMessage(new StringTextComponent("§4Error: §c" + message), Util.NIL_UUID);
        src.sendMessage(new StringTextComponent("§4Usage: §c/type <Pokémon name/number>"), Util.NIL_UUID);
        src.sendMessage(ERR_DELIMITER, Util.NIL_UUID);
    }
}
