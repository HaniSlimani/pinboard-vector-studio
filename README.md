# PinBoard — Éditeur Vectoriel

Éditeur de dessins vectoriels en Java/JavaFX, inspiré d'Adobe Illustrator et Inkscape.

Application de bureau permettant de créer des dessins composés de formes géométriques et d'images, avec outils de sélection, édition, et historique complet des modifications.

## Fonctionnalités

### Édition
- Dessin de **rectangles**, **ellipses** et **images**
- Outil de **sélection simple et multiple** (clic ou rectangle de sélection)
- **Déplacement** d'éléments à la souris
- **Grouper / dégrouper** des éléments (structure hiérarchique)
- **Copier / coller** avec presse-papier partagé entre fenêtres
- **Annuler / refaire** (undo/redo) sur une profondeur arbitraire
- Édition **multi-fenêtres** simultanée

### Interface
- Barre de menus (File, Edit, Tools...)
- Barre d'outils avec boutons de sélection
- Zone de dessin (Canvas JavaFX)
- Barre de statut contextuelle

## Architecture

Le projet applique plusieurs **design patterns** classiques :

| Pattern | Usage |
|---------|-------|
| **Stratégie** | Interface `Tool` — chaque outil (rectangle, ellipse, image, sélection) encapsule son propre comportement souris |
| **Commande** | Interface `Command` avec pile d'annulation (`CommandStack`) pour undo/redo |
| **Composite** | `ClipGroup` permet de traiter un groupe d'éléments comme un unique `Clip` |
| **Observateur** | `ClipboardListener` pour synchroniser les fenêtres via le presse-papier partagé |
| **Singleton** | `Clipboard` — presse-papier unique partagé entre toutes les fenêtres |

**Séparation modèle / vue** : le modèle de document (`pobj.pinboard.document`) est totalement indépendant de l'interface graphique (`pobj.pinboard.editor`), ce qui permettrait de réutiliser le modèle dans une autre application.

## Structure

```
src/pobj/pinboard/
├── document/              # Modèle de document (indépendant de la GUI)
│   ├── Clip.java          # Interface commune aux éléments graphiques
│   ├── AbstractClip.java  # Factorisation des fonctionnalités communes
│   ├── ClipRect.java      # Rectangle
│   ├── ClipEllipse.java   # Ellipse
│   ├── ClipImage.java     # Image (bonus)
│   ├── ClipGroup.java     # Groupe composite
│   └── Board.java         # Planche (conteneur de Clips)
│
├── editor/                # Interface graphique JavaFX
│   ├── EditorMain.java    # Point d'entrée
│   ├── EditorWindow.java  # Fenêtre d'édition
│   ├── Selection.java     # Gestion de la sélection
│   ├── Clipboard.java     # Presse-papier singleton
│   ├── CommandStack.java  # Pile undo/redo
│   ├── commands/          # Commandes (Add, Delete, Move, Group, Ungroup)
│   └── tools/             # Outils (Rectangle, Ellipse, Image, Selection)
│
└── test/                  # Tests d'affichage
```

## Exécution

### Prérequis
- Java 17+
- JavaFX 11+
- JUnit 5

### Sous Ubuntu/Debian
```bash
sudo apt install openjdk-17-jdk openjfx
```

### Lancer l'application
Importer le projet dans Eclipse, puis exécuter `EditorMain` avec *Run As > Java Application*.

Les tests unitaires sont situés dans les sous-packages `test/` de chaque module.

## Bonus implémentés

- **Support des images** — classe `ClipImage` + outil `ToolImage` avec sélecteur de fichiers

## Auteurs

- Hani Slimani
- [@akkaboutaina](https://github.com/akkaboutaina)
