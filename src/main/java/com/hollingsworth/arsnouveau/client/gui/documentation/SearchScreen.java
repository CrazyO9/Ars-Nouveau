package com.hollingsworth.arsnouveau.client.gui.documentation;

import com.hollingsworth.arsnouveau.api.documentation.DocClientUtils;
import com.hollingsworth.arsnouveau.api.documentation.entry.DocEntry;
import com.hollingsworth.arsnouveau.api.registry.DocumentationRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchScreen extends BaseDocScreen{
    List<DocEntryButton> searchResults = new ArrayList<>();
    public SearchScreen(String searchString){
        super();
        previousString = searchString;
    }

    @Override
    public void init() {
        super.init();
        searchBar.setValue(previousString);
        searchBar.mouseClicked(0, 0, 1);
        buildSearchResults();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        DocClientUtils.drawHeader(Component.translatable("ars_nouveau.doc.search_results"), graphics, bookLeft + LEFT_PAGE_OFFSET, bookTop + PAGE_TOP_OFFSET, ONE_PAGE_WIDTH, mouseX, mouseY, partialTicks);

    }

    public void buildSearchResults(){
        for(DocEntryButton button : searchResults){
            removeWidget(button);
        }
        searchResults.clear();
        List<DocEntry> searchStrings = new ArrayList<>(DocumentationRegistry.getEntries());
        searchStrings.removeIf(d -> StringUtils.getFuzzyDistance(previousString, d.entryTitle().getString(), Locale.ROOT) <= 0);
        searchStrings.sort((a, b) ->{
            return StringUtils.getFuzzyDistance(previousString, b.entryTitle().getString(), Locale.ROOT) - StringUtils.getFuzzyDistance(previousString, a.entryTitle().getString(), Locale.ROOT);
        });

        for(int i = 0; i < Math.min(searchStrings.size(), 8); i++){
            DocEntry entry = searchStrings.get(i);
            var button = new DocEntryButton(bookLeft + LEFT_PAGE_OFFSET, bookTop + PAGE_TOP_OFFSET  +  (16 * i) + 16, entry, (b) -> {
                previousScreen.transition(new PageHolderScreen(entry));
            });
            addRenderableWidget(button);
            searchResults.add(button);
        }

        for(int i = 0; i < Math.min(searchStrings.size() - 8, 9); i++){
            DocEntry entry = searchStrings.get(i + 8);
            var button = new DocEntryButton(bookLeft + RIGHT_PAGE_OFFSET, bookTop + PAGE_TOP_OFFSET  +  (16 * i), entry, (b) -> {
                previousScreen.transition(new PageHolderScreen(entry));
            });
            addRenderableWidget(button);
            searchResults.add(button);
        }
    }

    @Override
    public void onSearchChanged(String str) {
        if (str.equals(previousString))
            return;
        previousString = str;

        if(str.isEmpty()){
            previousScreen.previousString = "";
            goBack();
        }else {
            buildSearchResults();
        }
    }



    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        searchBar.setFocused(true);
        searchBar.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
