package com.feed_the_beast.ftblib.lib.util.text_components;

import java.util.HashMap;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;

/**
 * @author LatvianModder
 */
public class TextComponentParser {

    public static final NameMap.ObjectProperties<EnumChatFormatting> TEXT_FORMATTING_OBJECT_PROPERTIES = new NameMap.ObjectProperties<EnumChatFormatting>() {

        @Override
        public String getName(EnumChatFormatting value) {
            return value.getFriendlyName();
        }

        @Override
        public IChatComponent getDisplayName(@Nullable ICommandSender sender, EnumChatFormatting value) {
            return StringUtils.color(new ChatComponentText(getName(value)), value);
        }
    };

    public static final NameMap<EnumChatFormatting> TEXT_FORMATTING_NAME_MAP = NameMap
            .create(EnumChatFormatting.RESET, TEXT_FORMATTING_OBJECT_PROPERTIES, EnumChatFormatting.values());
    public static final NameMap<EnumChatFormatting> TEXT_FORMATTING_COLORS_NAME_MAP = NameMap.create(
            EnumChatFormatting.WHITE,
            TEXT_FORMATTING_OBJECT_PROPERTIES,
            EnumChatFormatting.BLACK,
            EnumChatFormatting.DARK_BLUE,
            EnumChatFormatting.DARK_GREEN,
            EnumChatFormatting.DARK_AQUA,
            EnumChatFormatting.DARK_RED,
            EnumChatFormatting.DARK_PURPLE,
            EnumChatFormatting.GOLD,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.DARK_GRAY,
            EnumChatFormatting.BLUE,
            EnumChatFormatting.GREEN,
            EnumChatFormatting.AQUA,
            EnumChatFormatting.RED,
            EnumChatFormatting.LIGHT_PURPLE,
            EnumChatFormatting.YELLOW,
            EnumChatFormatting.WHITE);

    public static final HashMap<Character, EnumChatFormatting> CODE_TO_FORMATTING = new HashMap<>();

    static {
        for (EnumChatFormatting formatting : TEXT_FORMATTING_NAME_MAP.values) {
            CODE_TO_FORMATTING.put(formatting.formattingCode, formatting);
        }
    }

    public static IChatComponent parse(String text, @Nullable Function<String, IChatComponent> substitutes) {
        return new TextComponentParser(text, substitutes).parse();
    }

    private String text;
    private Function<String, IChatComponent> substitutes;

    private IChatComponent component;
    private StringBuilder builder;
    private ChatStyle style;

    private TextComponentParser(String txt, @Nullable Function<String, IChatComponent> sub) {
        text = txt;
        substitutes = sub;
    }

    private IChatComponent parse() {
        if (text.isEmpty()) {
            return new ChatComponentText("");
        }

        char[] c = text.toCharArray();
        boolean hasSpecialCodes = false;

        for (char c1 : c) {
            if (c1 == '{' || c1 == '&' || c1 == StringUtils.FORMATTING_CHAR) {
                hasSpecialCodes = true;
                break;
            }
        }

        if (!hasSpecialCodes) {
            return new ChatComponentText(text);
        }

        component = new ChatComponentText("");
        style = new ChatStyle();
        builder = new StringBuilder();
        boolean sub = false;

        for (int i = 0; i < c.length; i++) {
            boolean escape = i > 0 && c[i - 1] == '\\';
            boolean end = i == c.length - 1;

            if (sub && (end || c[i] == '{' || c[i] == '}')) {
                if (c[i] == '{') {
                    throw new IllegalArgumentException("Invalid formatting! Can't nest multiple substitutes!");
                }

                finishPart();
                sub = false;
                continue;
            }

            if (!escape) {
                if (c[i] == '&') {
                    c[i] = StringUtils.FORMATTING_CHAR;
                }

                if (c[i] == StringUtils.FORMATTING_CHAR) {
                    finishPart();

                    if (end) {
                        throw new IllegalArgumentException(
                                "Invalid formatting! Can't end string with & or " + StringUtils.FORMATTING_CHAR + "!");
                    }

                    i++;

                    EnumChatFormatting formatting = CODE_TO_FORMATTING.get(c[i]);

                    if (formatting == null) {
                        throw new IllegalArgumentException(
                                "Illegal formatting! Unknown color code character: " + c[i] + "!");
                    }

                    switch (formatting) {
                        case OBFUSCATED:
                            style.setObfuscated(!style.getObfuscated());
                            break;
                        case BOLD:
                            style.setBold(!style.getBold());
                            break;
                        case STRIKETHROUGH:
                            style.setStrikethrough(!style.getStrikethrough());
                            break;
                        case UNDERLINE:
                            style.setUnderlined(!style.getUnderlined());
                            break;
                        case ITALIC:
                            style.setItalic(!style.getItalic());
                            break;
                        case RESET:
                            style = new ChatStyle();
                            break;
                        default:
                            style.setColor(formatting);
                    }

                    continue;
                } else if (c[i] == '{') {
                    finishPart();

                    if (end) {
                        throw new IllegalArgumentException("Invalid formatting! Can't end string with {!");
                    }

                    sub = true;
                }
            }

            if (c[i] != '\\' || escape) {
                builder.append(c[i]);
            }
        }

        finishPart();
        return component;
    }

    private void finishPart() {
        String string = builder.toString();
        builder.setLength(0);

        if (string.isEmpty()) {
            return;
        } else if (string.length() < 2 || string.charAt(0) != '{') {
            IChatComponent component1 = new ChatComponentText(string);
            component1.setChatStyle(style.createShallowCopy());
            component.appendSibling(component1);
            return;
        }

        IChatComponent component1 = substitutes.apply(string.substring(1));

        if (component1 != null) {
            // ChatStyle style0 = component1.getChatStyle().createShallowCopy();
            // ChatStyle style1 = style.createShallowCopy();
            // style1.setChatHoverEvent(style0.getChatHoverEvent());
            // style1.setChatClickEvent(style0.getChatClickEvent());
            // style1.setInsertion(style0.getFormattingCode());
            // component1.setChatStyle(style1);
            // is this pointless?
        } else {
            throw new IllegalArgumentException("Invalid formatting! Unknown substitute " + string);
        }

        component.appendSibling(component1);
    }
}
