# EAC1 - Projecte base alumne

Projecte Java per resoldre l'EAC1 del mòdul `0490. Programació de serveis i processos`.

## Paquet obligatori

```text
dam.psp.eac1
```

## Classes principals

- `Lliurament`: model d'un lliurament detectat, amb identificador, ruta i temps de processament. **No cal modificar-la**.
- `CarregadorLliuraments`: llegeix la carpeta `lliuraments/` i crea la llista de lliuraments a processar. **No cal modificar-la**.
- `CorrectorLliurament`: lògica comuna per validar un lliurament. **Cal completar-la**.
- `ValidadorLliurament`: eina externa que valida un lliurament reutilitzant `CorrectorLliurament`. **No cal modificar-la**.
- `CorrectorProcessos`: corrector basat en processos externs concurrents. **Cal completar-la**.
- `ResultatCorreccio`: resultat d'una correcció feta amb fils. **No cal modificar-la**.
- `TascaCorreccio`: tasca executable per fils que reutilitza `CorrectorLliurament`. **No cal modificar-la**.
- `MarcadorCorreccions`: estat compartit de les correccions. **Cal completar-la**.
- `CorrectorExecutor`: corrector basat en `ExecutorService`. **Cal completar-la**.

## Carpeta de lliuraments

El projecte inclou una carpeta `lliuraments/` amb dades simulades:

```text
lliuraments/
  alumne01/
    lliurament.txt
  alumne02/
    lliurament.txt
  ...
```

Cada carpeta representa el lliurament d'un alumne. El fitxer `lliurament.txt` conté una paraula:

- `correcte`: el lliurament s'ha de considerar vàlid.
- `error`: el lliurament s'ha de considerar incorrecte.

La carpeta `lliuraments/` és la **font de veritat**. El resultat no està guardat en cap objecte Java: s'ha de deduir llegint el fitxer `lliurament.txt`.

## Flux de dades

El programa descobreix els lliuraments abans de processar-los:

```text
CarregadorLliuraments.carregar()
  -> llegeix lliuraments/
  -> detecta carpetes d'alumnes
  -> crea objectes Lliurament
  -> retorna List<Lliurament>
```

Després, aquesta llista es pot processar de dues maneres:

```text
CorrectorProcessos -> llança diversos ValidadorLliurament com a processos externs
CorrectorExecutor  -> envia TascaCorreccio a un pool de fils
```

`Lliurament` només conté l'identificador, la ruta i el temps simulat. `CarregadorLliuraments` només descobreix carpetes. Ni una classe ni l'altra decideixen si el lliurament és correcte o incorrecte.

## Estructura corregible

El projecte de les preguntes 1, 2, 3 i 4 està preparat perquè es pugui corregir de manera **automàtica**. Per això:

- no creeu classes noves,
- no canvieu els noms de paquets, classes o mètodes,
- no canvieu les signatures dels mètodes públics,
- valideu les entrades nules, buides o invàlides dels mètodes públics,
- completeu només els punts marcats amb `TODO` en aquest projecte.

Cada exercici té uns mètodes concrets associats:

| Exercici | Mètodes principals |
| --- | --- |
| Exercici 1 | `CorrectorLliurament.obtenirRutaFitxerLliurament(...)`, `llegirContingut(...)`, `esLliuramentCorrecte(...)` |
| Exercici 2 | `CorrectorProcessos.construirOrdreValidador(...)`, `prepararProces(...)`, `esperarProces(...)`, `corregirLliuraments(...)` |
| Exercici 3 | `CorrectorExecutor.enviarTasques(...)`, `recollirResultats(...)`, `tancarExecutor(...)` |
| Exercici 4 | `MarcadorCorreccions.registrar(...)` i mètodes de consulta |

## Fitxer de respostes

El fitxer `respostes_teoriques.md` forma part del lliurament. Cal completar-hi les respostes teoricopràctiques dels exercicis 1, 2, 3 i 4.

## Com compilar

```bash
mvn compile
```

## Com executar exemples

Validador individual:

```bash
java -cp target/classes dam.psp.eac1.ValidadorLliurament alumne01 lliuraments/alumne01
```

Corrector amb processos:

```bash
java -cp target/classes dam.psp.eac1.CorrectorProcessos
```

Corrector amb executor:

```bash
java -cp target/classes dam.psp.eac1.CorrectorExecutor 3
```

## Nota

El projecte compila inicialment, però no resol tota l'EAC. Cal completar els punts marcats amb `TODO`.
