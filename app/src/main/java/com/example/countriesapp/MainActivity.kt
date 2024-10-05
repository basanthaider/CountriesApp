package com.example.countriesapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.countriesapp.data.DataSource
import com.example.countriesapp.model.Country
import com.example.countriesapp.ui.theme.CountriesAppTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountriesAppTheme {
                Scaffold { innerPadding ->
                    CountriesList(
                        countries = DataSource().getCountries(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}

@Composable
fun CountriesList(countries: List<Country>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = modifier) {
            items(countries) { country ->
                CountryCard(country = country)
            }
        }

        val annotatedString =
            buildAnnotatedString {
                append("Check the countries' population from ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )

                ) {
                    pushStringAnnotation(
                        "link_tag",
                        "https://www.worldometers.info/geography/how-many-countries-are-there-in-the-world/"
                    )
                    append("here")
                    pop()
                }
            }
        ClickableText(
            text = annotatedString
        ){
            offset->annotatedString.getStringAnnotations(tag = "link_tag", start = offset, end = offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, it.item.toUri())
                    context.startActivity(intent)
            }
        }
    }
}

@Composable
fun CountryCard(country: Country, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = country.pic),
                contentDescription = stringResource(id = country.name),
                modifier = modifier
                    .padding(start = 16.dp)
                    .size(80.dp)
            )
            Text(
                text = stringResource(id = country.name),
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = modifier.padding(16.dp)
            )
            IconButton(
                onClick = {
                    openMap(country.lat, country.long, context)
                }, modifier = modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Get Location",
                    tint = Color.Unspecified
                )
            }
        }
    }
}


/*@Preview
@Composable
fun CountryCardPreview() {
    CountryCard(country = DataSource().getCountries()[0])
}*/

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CountriesListPreview() {
    CountriesList(countries = DataSource().getCountries())
}

fun openMap(lat: Double, long: Double, c: Context) {
    try {
        val gmmIntentUri =
            "google.navigation:q=YAT Learning Centers - Maadi".toUri()// Create a Uri from an intent string. Use the result to create an Intent.
        //instead of "google.streetview:cbll" use geo to open the normal maps
        val mapIntent = Intent(
            Intent.ACTION_VIEW, gmmIntentUri
        )// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        mapIntent.setPackage("com.google.android.apps.maps")// Make the Intent explicit by setting the Google Maps package
        c.startActivity(mapIntent)// Attempt to start an activity that can handle the Intent
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(c, "google maps is not installed!!!", Toast.LENGTH_SHORT).show()
        c.startActivity(
            Intent(
                Intent.ACTION_VIEW, "market://details?id=com.google.android.apps.maps".toUri()
            )
        )
    }


}

